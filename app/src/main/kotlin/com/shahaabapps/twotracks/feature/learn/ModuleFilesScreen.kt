package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.data.files.ModuleFilesRepository
import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.SessionStore
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/** The files attached to one module, and whatever is on its way up. */
data class ModuleFilesUiState(
    val courseTitle: String = "",
    val moduleTitle: String = "",
    val files: List<ModuleFile> = emptyList(),
    /** The fraction of the upload in flight, or null when nothing is going up. */
    val uploading: Float? = null,
    val message: String? = null,
    val loaded: Boolean = false,
)

@HiltViewModel
class ModuleFilesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: SessionStore,
    private val repository: ModuleFilesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val courseId = savedStateHandle.get<String>("courseId").orEmpty()
    private val moduleId = savedStateHandle.get<String>("moduleId").orEmpty()
    private val uploading = MutableStateFlow<Float?>(null)
    private val message = MutableStateFlow<String?>(null)

    private val course = Courses.all.firstOrNull { it.id == courseId }
    private val module = course?.modules?.firstOrNull { it.id == moduleId }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ModuleFilesUiState> =
        combine(
            sessionStore.session.flatMapLatest { s ->
                if (s == null) flowOf(null) else repository.observe(s.coupleId, courseId, moduleId)
            },
            uploading,
            message,
        ) { files, progress, msg ->
            ModuleFilesUiState(
                courseTitle = course?.title.orEmpty(),
                moduleTitle = module?.title.orEmpty(),
                files = files.orEmpty(),
                uploading = progress,
                message = msg,
                loaded = files != null,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ModuleFilesUiState())

    /**
     * Uploads what the picker handed back.
     *
     * The name, the type and the size all come from the content resolver
     * rather than from the uri, because a document uri's last path segment is
     * a provider's own id and is not a file name in any useful sense.
     */
    fun add(uri: Uri) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val (name, size) = describe(uri)
        val file = ModuleFile(
            courseId = courseId, moduleId = moduleId, name = name,
            mimeType = context.contentResolver.getType(uri).orEmpty(),
            sizeBytes = size, uploadedBy = session.uid, uploadedAtIso = Instant.now().toString(),
        )
        uploading.value = 0f
        val result = repository.upload(session.coupleId, file, uri) { uploading.value = it }
        uploading.value = null
        message.value = when (result) {
            is AppResult.Success -> "${file.name} added"
            is AppResult.Failure -> "Could not add ${file.name}"
        }
    }

    fun delete(file: ModuleFile) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        repository.delete(session.coupleId, file)
        message.value = "${file.name} removed"
    }

    /** Hands the file to whatever app opens that kind, by a link the viewer can fetch. */
    fun open(file: ModuleFile) = viewModelScope.launch {
        when (val url = repository.url(file)) {
            is AppResult.Success -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.data))
                    .setDataAndType(Uri.parse(url.data), file.mimeType.ifBlank { "*/*" })
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                runCatching { context.startActivity(intent) }
                    .onFailure { message.value = "Nothing on this phone opens ${file.name}" }
            }
            is AppResult.Failure -> message.value = "Could not open ${file.name}"
        }
    }

    fun messageShown() { message.value = null }

    /** The display name and size the provider reports, with sane fallbacks. */
    private fun describe(uri: Uri): Pair<String, Long> {
        val cursor = runCatching {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)
        }.getOrNull() ?: return uri.lastPathSegment.orEmpty().substringAfterLast('/') to 0L
        return cursor.use { c ->
            if (!c.moveToFirst()) return@use "File" to 0L
            val name = c.getColumnIndex(OpenableColumns.DISPLAY_NAME).takeIf { it >= 0 }?.let { c.getString(it) }
            val size = c.getColumnIndex(OpenableColumns.SIZE).takeIf { it >= 0 && !c.isNull(it) }?.let { c.getLong(it) }
            (name?.takeIf { it.isNotBlank() } ?: "File") to (size ?: 0L)
        }
    }
}

@Composable
fun ModuleFilesScreen(
    onBack: () -> Unit,
    onView: (String) -> Unit,
    viewModel: ModuleFilesViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        // The picker's grant ends with the activity unless it is held, and the
        // upload outlives this composition.
        uri?.let {
            runCatching { context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            viewModel.add(it)
        }
    }
    ModuleFilesContent(
        state = state,
        onBack = onBack,
        onAdd = { picker.launch(PICKABLE) },
        // Pictures and PDFs are drawn in the app; anything else is handed to
        // whatever on the phone knows what to do with it.
        onOpen = { if (it.isViewable) onView(it.id) else viewModel.open(it) },
        onDelete = viewModel::delete,
        onMessageShown = viewModel::messageShown,
    )
}

/**
 * What the two of you have attached to a module, drawn from state alone.
 *
 * It is a plain list on purpose. The things that land here are a diagram
 * from the exam guide, a photo of a whiteboard, a PDF somebody sent — each
 * wants a name, a size and a way to open it, and nothing else earns the
 * room.
 */
@Composable
fun ModuleFilesContent(
    state: ModuleFilesUiState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpen: (ModuleFile) -> Unit,
    onDelete: (ModuleFile) -> Unit,
    onMessageShown: () -> Unit = {},
) {
    // Removing a file takes the bytes with it and there is no undo, so the
    // bin asks first. It sits a few millimetres from the row that opens the
    // file, which is exactly the distance a thumb gets wrong.
    var confirming by remember { mutableStateOf<ModuleFile?>(null) }
    DetailScaffold(
        title = state.moduleTitle.ifBlank { "Files" },
        onBack = onBack,
        eyebrow = listOfNotNull(state.courseTitle.ifBlank { null }, "FILES").joinToString(" · ").uppercase(),
        modifier = Modifier.testTag(MODULE_FILES_SCREEN),
    ) { inner ->
        val snackbar = remember { SnackbarHostState() }
        LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }
        Box(Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = Spacing.listHorizontal, end = Spacing.listHorizontal,
                top = inner.calculateTopPadding() + Spacing.sm, bottom = Spacing.xl * 2,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            item {
                CupertinoPrimaryButton(text = "Add a file", onClick = onAdd)
            }
            state.uploading?.let { fraction ->
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = Spacing.sm)) {
                        Text("Uploading…", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                        LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth().testTag(UPLOAD_BAR))
                    }
                }
            }
            if (state.files.isEmpty() && state.uploading == null) {
                item {
                    Text(
                        "Nothing here yet. Anything you add — a diagram, a cheat sheet, a photo of a whiteboard — is kept in your shared space and shows up for both of you.",
                        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.padding(top = Spacing.sm),
                    )
                }
            }
            items(state.files.size) { i -> FileRow(state.files[i], onOpen) { confirming = it } }
        }
        SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
    confirming?.let { file ->
        RemoveFileDialog(
            file = file,
            onConfirm = { confirming = null; onDelete(file) },
            onDismiss = { confirming = null },
        )
    }
}

@Composable
private fun FileRow(file: ModuleFile, onOpen: (ModuleFile) -> Unit, onAskDelete: (ModuleFile) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClickLabel = "Open ${file.name}", onClick = { onOpen(file) })
            .padding(vertical = 10.dp),
    ) {
        Icon(
            if (file.isImage) Icons.Filled.Image else Icons.Filled.Description,
            contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(22.dp),
        )
        Column(modifier = Modifier.weight(1f).padding(horizontal = Spacing.sm)) {
            Text(file.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(file.readableSize, style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clickable(role = Role.Button, onClickLabel = "Remove ${file.name}", onClick = { onAskDelete(file) }),
        ) {
            Icon(Icons.Filled.Delete, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(20.dp))
        }
    }
}

/**
 * What the picker offers. Images and PDFs are what these are in practice;
 * the office types are there because a colleague's cheat sheet arrives as
 * one often enough, and plain text because notes do.
 */
private val PICKABLE = arrayOf(
    "image/*",
    "application/pdf",
    "text/plain",
    "application/msword",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "application/vnd.ms-powerpoint",
    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
)

const val MODULE_FILES_SCREEN = "module-files-screen"
const val UPLOAD_BAR = "module-files-upload"

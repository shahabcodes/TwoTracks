package com.shahaabapps.twotracks.feature.learn

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.data.ink.InkNotesRepository
import com.shahaabapps.core.model.InkNote
import com.shahaabapps.core.model.InkPage
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/** A page of handwriting open for writing on. */
data class InkNoteUiState(
    val note: InkNote = InkNote(),
    val page: InkPage = InkPage(),
    val tool: InkTool = InkTool(),
    val heading: String = "",
    /** True once the strokes are here, so an empty page is not shown over a loading one. */
    val loaded: Boolean = false,
    val saving: Boolean = false,
    /** Whether anything has been written since the last save. */
    val unsaved: Boolean = false,
    val message: String? = null,
)

@HiltViewModel
class InkNoteViewModel @Inject constructor(
    private val sessionStore: SessionStore,
    private val repository: InkNotesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val courseId = savedStateHandle.get<String>("courseId").orEmpty()
    private val moduleId = savedStateHandle.get<String>("moduleId").orEmpty()
    private val topicId = savedStateHandle.get<String>("topicId").orEmpty()
    private val noteId = savedStateHandle.get<String>("noteId").orEmpty()

    private val course = Courses.all.firstOrNull { it.id == courseId }
    private val module = course?.modules?.firstOrNull { it.id == moduleId }
    private val topic = module?.topics?.firstOrNull { it.id == topicId }

    private val state = MutableStateFlow(
        InkNoteUiState(heading = topic?.title ?: module?.title.orEmpty(), loaded = noteId.isBlank()),
    )
    val uiState: StateFlow<InkNoteUiState> = state.asStateFlow()

    /**
     * Each page as it was before the last change, so a stroke or a rubbing
     * out can be taken back.
     *
     * Capped, because a page of handwriting is hundreds of strokes and
     * keeping every state of it is keeping the page hundreds of times. Far
     * enough back to undo a mistake, not far enough to undo the afternoon.
     */
    private val history = ArrayDeque<InkPage>()

    init {
        if (noteId.isNotBlank()) {
            viewModelScope.launch {
                val session = sessionStore.current()
                // The list this screen was opened from, read once: there is
                // no get-by-id on the repository because nothing else wants
                // one, and the list is already in the Firestore cache here.
                val found = session?.let { s ->
                    repository.observe(s.coupleId, courseId, moduleId).first().firstOrNull { it.id == noteId }
                }
                if (found == null) {
                    state.value = state.value.copy(loaded = true, message = "That note is no longer here")
                    return@launch
                }
                when (val page = repository.load(found)) {
                    is AppResult.Success ->
                        state.value = state.value.copy(note = found, page = page.data, loaded = true)
                    is AppResult.Failure -> {
                        Log.w("InkNote", "loading ${found.title} failed", page.error)
                        state.value = state.value.copy(
                            note = found, loaded = true,
                            message = "Could not open ${found.title} — ${reason(page.error)}",
                        )
                    }
                }
            }
        }
    }

    fun write(page: InkPage) {
        history.addLast(state.value.page)
        if (history.size > UNDO_DEPTH) history.removeFirst()
        state.value = state.value.copy(page = page, unsaved = true)
    }

    fun undo() {
        val previous = history.removeLastOrNull() ?: return
        state.value = state.value.copy(page = previous, unsaved = true)
    }

    fun pick(tool: InkTool) { state.value = state.value.copy(tool = tool) }

    /** Saves under [title], which is the note's own if it already has one. */
    fun save(title: String) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val now = Instant.now().toString()
        val current = state.value
        val note = current.note.copy(
            courseId = courseId, moduleId = moduleId, topicId = topicId,
            title = title.trim().ifBlank { "Note" },
            createdBy = current.note.createdBy.ifBlank { session.uid },
            createdAtIso = current.note.createdAtIso.ifBlank { now },
            updatedAtIso = now,
        )
        state.value = current.copy(saving = true)
        state.value = when (val result = repository.save(session.coupleId, note, current.page)) {
            is AppResult.Success ->
                state.value.copy(note = result.data, saving = false, unsaved = false, message = "${note.title} saved")
            is AppResult.Failure -> {
                Log.w("InkNote", "saving ${note.title} failed", result.error)
                state.value.copy(saving = false, message = "Could not save ${note.title} — ${reason(result.error)}")
            }
        }
    }

    /** Removes the note, reporting back only once it is actually gone. */
    fun delete(onGone: () -> Unit) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val note = state.value.note
        if (note.id.isBlank()) { onGone(); return@launch }
        when (val result = repository.delete(session.coupleId, note)) {
            is AppResult.Success -> onGone()
            is AppResult.Failure -> {
                Log.w("InkNote", "removal of ${note.title} failed", result.error)
                state.value = state.value.copy(message = "Could not remove ${note.title} — ${reason(result.error)}")
            }
        }
    }

    fun messageShown() { state.value = state.value.copy(message = null) }

    private companion object {
        const val UNDO_DEPTH = 40
    }
}

@Composable
fun InkNoteScreen(onBack: () -> Unit, viewModel: InkNoteViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    InkNoteContent(
        state = state,
        onBack = onBack,
        onWrite = viewModel::write,
        onUndo = viewModel::undo,
        onTool = viewModel::pick,
        onSave = viewModel::save,
        onDelete = { viewModel.delete(onGone = onBack) },
        onMessageShown = viewModel::messageShown,
    )
}

/**
 * The page, and as little around it as the job allows.
 *
 * Everything that is not paper is a strip at the top and a strip at the
 * foot, because the thing being made here is the page: a toolbar down one
 * side would take a third of a phone held upright, which is a third of the
 * space somebody is trying to write a worked example in.
 *
 * It asks for a name on the way out rather than on the way in. A blank page
 * with a naming dialog over it is a question nobody can answer — the note
 * does not exist yet to be named after anything — and a page with something
 * on it names itself.
 */
@Composable
fun InkNoteContent(
    state: InkNoteUiState,
    onBack: () -> Unit,
    onWrite: (InkPage) -> Unit,
    onUndo: () -> Unit,
    onTool: (InkTool) -> Unit,
    onSave: (String) -> Unit,
    onDelete: () -> Unit,
    onMessageShown: () -> Unit = {},
) {
    var naming by remember { mutableStateOf(false) }
    var confirming by remember { mutableStateOf(false) }
    var leaving by remember { mutableStateOf(false) }

    /**
     * Leaving is a question while there is unsaved handwriting on the page.
     *
     * Everywhere else in the app, backing out of a screen loses nothing: the
     * work is already written down. Here the strokes exist nowhere but this
     * composition until they are saved, and the back arrow is two
     * centimetres from the tick.
     */
    fun leave() { if (state.unsaved) leaving = true else onBack() }
    BackHandler(enabled = state.unsaved) { leaving = true }
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); onMessageShown() } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.groupedBackground)
            .testTag(INK_NOTE_SCREEN),
    ) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = 6.dp),
            ) {
                InkAction(Icons.AutoMirrored.Filled.ArrowBack, "Back", ::leave)
                Column(modifier = Modifier.weight(1f).padding(horizontal = Spacing.xs)) {
                    Text(
                        state.note.title.ifBlank { "New note" },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        listOfNotNull(
                            state.heading.takeIf { it.isNotBlank() },
                            if (state.unsaved) "unsaved" else state.page.strokes.size.takeIf { it > 0 }?.let { "$it strokes" },
                        ).joinToString(" · ").uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.secondaryLabel,
                        maxLines = 1,
                    )
                }
                InkAction(Icons.AutoMirrored.Filled.Undo, "Undo the last stroke", onUndo)
                if (state.note.id.isNotBlank()) {
                    InkAction(Icons.Filled.Delete, "Remove this note", { confirming = true })
                }
                if (state.saving) {
                    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                    }
                } else {
                    InkAction(
                        Icons.Filled.Check, "Save this note",
                        { if (state.note.title.isBlank()) naming = true else onSave(state.note.title) },
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = Spacing.sm),
                contentAlignment = Alignment.TopCenter,
            ) {
                if (!state.loaded) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = Spacing.xl))
                } else {
                    InkCanvas(
                        page = state.page,
                        tool = state.tool,
                        onPage = onWrite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(state.page.width / state.page.height)
                            .clip(RoundedCornerShape(10.dp))
                            .border(Spacing.rule, AppTheme.colors.separator, RoundedCornerShape(10.dp)),
                    )
                }
            }

            InkTools(state.tool, onTool, modifier = Modifier.navigationBarsPadding())
        }
        SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 72.dp))
    }

    if (leaving) {
        LeaveNoteDialog(
            named = state.note.title.isNotBlank(),
            onSave = {
                leaving = false
                if (state.note.title.isBlank()) naming = true else onSave(state.note.title)
            },
            onDiscard = { leaving = false; onBack() },
            onDismiss = { leaving = false },
        )
    }
    if (naming) {
        NameNoteDialog(
            suggestion = state.heading,
            onConfirm = { name -> naming = false; onSave(name) },
            onDismiss = { naming = false },
        )
    }
    if (confirming) {
        RemoveNoteDialog(
            note = state.note,
            onConfirm = { confirming = false; onDelete() },
            onDismiss = { confirming = false },
        )
    }
}

/** The pen: four inks, a weight, and the eraser, as one strip at the foot. */
@Composable
private fun InkTools(tool: InkTool, onTool: (InkTool) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterHorizontally),
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.sm),
    ) {
        for (ink in INKS) {
            val chosen = !tool.erasing && tool.colour == ink
            Box(
                modifier = Modifier
                    .size(if (chosen) 30.dp else 26.dp)
                    .clip(CircleShape)
                    .background(colourOf(ink))
                    .border(
                        if (chosen) 2.dp else Spacing.rule,
                        if (chosen) AppTheme.colors.accent else AppTheme.colors.separator,
                        CircleShape,
                    )
                    .clickable(role = Role.Button, onClickLabel = "Write in ${NAMES[ink]}") {
                        onTool(tool.copy(colour = ink, erasing = false))
                    },
            )
        }
        for (width in WIDTHS) {
            val chosen = !tool.erasing && tool.width == width
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (chosen) AppTheme.colors.fill else Color.Transparent)
                    .clickable(role = Role.Button, onClickLabel = "A ${WIDTH_NAMES[width]} nib") {
                        onTool(tool.copy(width = width, erasing = false))
                    },
            ) {
                Box(
                    modifier = Modifier
                        .size((width * 2.2f).dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.ink),
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (tool.erasing) AppTheme.colors.fill else Color.Transparent)
                .clickable(role = Role.Button, onClickLabel = "Rub out") {
                    onTool(tool.copy(erasing = !tool.erasing))
                },
        ) {
            Icon(
                Icons.Filled.AutoFixNormal, contentDescription = null,
                tint = if (tool.erasing) AppTheme.colors.accentText else AppTheme.colors.secondaryLabel,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun InkAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) {
        Icon(icon, contentDescription = label, tint = AppTheme.colors.ink, modifier = Modifier.size(22.dp))
    }
}

/** Ink, red for marking, blue for working, and a highlighter's yellow. */
private val INKS = listOf("1C1C20", "C0392B", "1F5FC4", "E3B505")
private val NAMES = mapOf(
    "1C1C20" to "ink", "C0392B" to "red", "1F5FC4" to "blue", "E3B505" to "yellow",
)
private val WIDTHS = listOf(2f, 3.2f, 6f)
private val WIDTH_NAMES = mapOf(2f to "fine", 3.2f to "medium", 6f to "broad")

const val INK_NOTE_SCREEN = "ink-note-screen"

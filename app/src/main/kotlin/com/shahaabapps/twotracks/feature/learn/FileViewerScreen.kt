package com.shahaabapps.twotracks.feature.learn

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.shahaabapps.core.common.AppResult
import com.shahaabapps.core.data.files.ModuleFilesRepository
import com.shahaabapps.core.model.ModuleFile
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * One attachment, shown in the app.
 *
 * [local] is the file on this phone: images and PDFs are both drawn from
 * bytes rather than from a link, so the page is sharp, works offline the
 * second time, and a PDF can be handed to the renderer at all.
 */
data class FileViewerUiState(
    val file: ModuleFile? = null,
    /** Everything viewable in the same module, so the arrows can move between them. */
    val siblings: List<ModuleFile> = emptyList(),
    val local: File? = null,
    /** The fraction of the download in flight, or null when it is not downloading. */
    val downloading: Float? = null,
    val error: String? = null,
) {
    val position: Int get() = siblings.indexOfFirst { it.id == file?.id }
    val hasPrevious: Boolean get() = position > 0
    val hasNext: Boolean get() = position >= 0 && position < siblings.lastIndex
}

@HiltViewModel
class FileViewerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: SessionStore,
    private val repository: ModuleFilesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val courseId = savedStateHandle.get<String>("courseId").orEmpty()
    private val moduleId = savedStateHandle.get<String>("moduleId").orEmpty()

    /** Set when the viewer was opened from a lesson, so the arrows stay in it. */
    private val topicId = savedStateHandle.get<String>("topicId").orEmpty()

    private val currentId = MutableStateFlow(savedStateHandle.get<String>("fileId").orEmpty())
    private val local = MutableStateFlow<File?>(null)
    private val downloading = MutableStateFlow<Float?>(null)
    private val error = MutableStateFlow<String?>(null)

    /**
     * The module's attachments the app can draw itself, in the list's own
     * order. Anything else was never opened here, so it is not something the
     * arrows should land on.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val siblings: StateFlow<List<ModuleFile>> =
        sessionStore.session.flatMapLatest { s ->
            if (s == null) flowOf(emptyList()) else repository.observe(s.coupleId, courseId, moduleId)
        }
            .map { files ->
                files.filter { it.isViewable && (topicId.isBlank() || it.topicId == topicId) }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val uiState: StateFlow<FileViewerUiState> =
        combine(siblings, currentId, local, downloading, error) { list, id, cached, progress, err ->
            FileViewerUiState(
                file = list.firstOrNull { it.id == id },
                siblings = list,
                local = cached,
                downloading = progress,
                error = err,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FileViewerUiState())

    init {
        viewModelScope.launch {
            combine(siblings, currentId) { list, id -> list.firstOrNull { it.id == id } }
                .distinctUntilChanged { a, b -> a?.id == b?.id && a?.path == b?.path }
                // collectLatest, so moving to the next picture abandons the
                // download of the one just left rather than queueing behind it.
                .collectLatest { file ->
                    local.value = null
                    error.value = null
                    if (file == null) return@collectLatest
                    downloading.value = 0f
                    when (val result = repository.localFile(file) { downloading.value = it }) {
                        is AppResult.Success -> local.value = result.data
                        is AppResult.Failure -> {
                            Log.w("FileViewer", "download of ${file.name} failed", result.error)
                            error.value = "Could not download ${file.name} — ${reason(result.error)}"
                        }
                    }
                    downloading.value = null
                }
        }
    }

    fun previous() {
        val list = siblings.value
        val at = list.indexOfFirst { it.id == currentId.value }
        if (at > 0) currentId.value = list[at - 1].id
    }

    fun next() {
        val list = siblings.value
        val at = list.indexOfFirst { it.id == currentId.value }
        if (at >= 0 && at < list.lastIndex) currentId.value = list[at + 1].id
    }

    /** Hands the file to another app, which is also how it is saved or shared. */
    fun openElsewhere() = viewModelScope.launch {
        val file = uiState.value.file ?: return@launch
        when (val url = repository.url(file)) {
            is AppResult.Success -> {
                val uri = Uri.parse(url.data)
                val intent = Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri, file.mimeType.ifBlank { "*/*" })
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                runCatching { context.startActivity(intent) }
                    .onFailure { error.value = "Nothing on this phone opens ${file.name}" }
            }
            is AppResult.Failure -> error.value = "Could not open ${file.name} — ${reason(url.error)}"
        }
    }

    /** Gives the file another name, without touching the bytes under it. */
    fun rename(name: String) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val file = uiState.value.file ?: return@launch
        val wanted = name.trim()
        if (wanted.isEmpty() || wanted == file.name) return@launch
        val result = repository.rename(session.coupleId, file, wanted)
        if (result is AppResult.Failure) {
            Log.w("FileViewer", "rename of ${file.name} failed", result.error)
            error.value = "Could not rename ${file.name} — ${reason(result.error)}"
        }
    }

    /**
     * Removes the file, and reports whether anything is left to look at so
     * the screen knows to close rather than show an empty frame.
     */
    fun delete(onEmpty: () -> Unit) = viewModelScope.launch {
        val session = sessionStore.current() ?: return@launch
        val file = uiState.value.file ?: return@launch
        val list = siblings.value
        val at = list.indexOfFirst { it.id == file.id }
        // Chosen before the delete lands, because the list this reads from is
        // about to change underneath it: the one after, or failing that the
        // one before, which is where a person's eye already is.
        val nextId = list.getOrNull(at + 1)?.id ?: list.getOrNull(at - 1)?.id
        when (val result = repository.delete(session.coupleId, file)) {
            is AppResult.Success -> if (nextId == null) onEmpty() else currentId.value = nextId
            is AppResult.Failure -> {
                Log.w("FileViewer", "removal of ${file.name} failed", result.error)
                // Stays on the file: it is still there, and leaving the screen
                // as though it had gone is how a person stops trusting either.
                error.value = "Could not remove ${file.name} — ${reason(result.error)}"
            }
        }
    }
}

@Composable
fun FileViewerScreen(onBack: () -> Unit, viewModel: FileViewerViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FileViewerContent(
        state = state,
        onBack = onBack,
        onPrevious = viewModel::previous,
        onNext = viewModel::next,
        onOpenElsewhere = viewModel::openElsewhere,
        onRename = viewModel::rename,
        onDelete = { viewModel.delete(onEmpty = onBack) },
    )
}

/**
 * The attachment itself, as large as the screen allows.
 *
 * Both looks draw it the same way, and deliberately: a photograph of a
 * whiteboard and a page of a PDF are judged against the dark around them, and
 * a page that changed colour with the app's look would be a page that lied
 * about what is on it. The chrome is the app's — the back arrow, the actions
 * and the accent all come from the theme — and it sits over the picture
 * rather than beside it, because the picture is why the screen exists.
 */
@Composable
fun FileViewerContent(
    state: FileViewerUiState,
    onBack: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onOpenElsewhere: () -> Unit,
    onRename: (String) -> Unit = {},
    onDelete: () -> Unit,
) {
    var confirming by remember { mutableStateOf(false) }
    var renaming by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf(1 to 1) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SURROUND)
            .testTag(FILE_VIEWER_SCREEN),
    ) {
        val file = state.file
        when {
            state.error != null -> Message(state.error)
            state.downloading != null -> Downloading(state.downloading, file?.name.orEmpty())
            file == null || state.local == null -> Message("Nothing to show here.")
            file.isImage -> ZoomableImage(state.local, file.name)
            else -> PdfPages(state.local, onPage = { at, of -> page = at to of })
        }

        // The chrome sits over the picture and so over the status bar too,
        // which is why it is inset here rather than by the scaffold: the
        // picture itself is meant to go under both.
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).background(SCRIM).statusBarsPadding()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = 6.dp),
            ) {
                RoundAction(Icons.AutoMirrored.Filled.ArrowBack, "Back", onBack)
                Column(modifier = Modifier.weight(1f).padding(horizontal = Spacing.xs)) {
                    Text(
                        file?.name.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val line = listOfNotNull(
                        file?.readableSize,
                        if (state.siblings.size > 1) "${state.position + 1} of ${state.siblings.size}" else null,
                        if (file?.isPdf == true && state.local != null) "page ${page.first} of ${page.second}" else null,
                    ).joinToString(" · ")
                    if (line.isNotEmpty()) Text(
                        line.uppercase(),
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = Color.White.copy(alpha = 0.62f),
                    )
                }
                if (file != null) {
                    RoundAction(Icons.Filled.DriveFileRenameOutline, "Rename ${file.name}", { renaming = true })
                    RoundAction(Icons.Filled.OpenInNew, "Open ${file.name} in another app", onOpenElsewhere)
                    RoundAction(Icons.Filled.Delete, "Remove ${file.name}", { confirming = true })
                }
            }
        }

        if (state.siblings.size > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = Spacing.md)
                    .clip(RoundedCornerShape(24.dp))
                    .background(PILL)
                    .padding(horizontal = Spacing.sm, vertical = 4.dp),
            ) {
                if (state.hasPrevious) RoundAction(Icons.Filled.ChevronLeft, "Previous file", onPrevious)
                else Spacer(Modifier.size(40.dp))
                Text(
                    "${state.position + 1} / ${state.siblings.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f),
                )
                if (state.hasNext) RoundAction(Icons.Filled.ChevronRight, "Next file", onNext)
                else Spacer(Modifier.size(40.dp))
            }
        }
    }
    if (renaming && state.file != null) {
        RenameFileDialog(
            file = state.file,
            onConfirm = { name -> renaming = false; onRename(name) },
            onDismiss = { renaming = false },
        )
    }
    if (confirming && state.file != null) {
        RemoveFileDialog(
            file = state.file,
            preview = state.local,
            onConfirm = { confirming = false; onDelete() },
            onDismiss = { confirming = false },
        )
    }
}

/** A picture, pinched to zoom, dragged to move, double-tapped to fill or fit. */
@Composable
private fun ZoomableImage(file: File, name: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var box by remember { mutableStateOf(IntSize.Zero) }

    /**
     * Keeps the picture's edges from being dragged inside the frame: at a
     * given scale there is only so much of it off screen to pull back, and
     * past that the gesture should do nothing rather than fling it away.
     */
    fun clamp(wanted: Offset, at: Float): Offset {
        val maxX = (box.width * (at - 1f) / 2f).coerceAtLeast(0f)
        val maxY = (box.height * (at - 1f) / 2f).coerceAtLeast(0f)
        return Offset(wanted.x.coerceIn(-maxX, maxX), wanted.y.coerceIn(-maxY, maxY))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { box = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { at ->
                        if (scale > 1.05f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            // Zoomed about the point that was tapped, so the
                            // word being squinted at is the word that grows.
                            val centre = Offset(box.width / 2f, box.height / 2f)
                            scale = DOUBLE_TAP_SCALE
                            offset = clamp((centre - at) * (DOUBLE_TAP_SCALE - 1f), DOUBLE_TAP_SCALE)
                        }
                    },
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, gestureZoom, _ ->
                    val next = (scale * gestureZoom).coerceIn(1f, MAX_SCALE)
                    // Held about the fingers, not the middle of the frame. The
                    // layer scales around its own centre, so the point under
                    // the centroid is only still under it if the picture is
                    // moved by the distance that scaling pushed it away.
                    val middle = Offset(box.width / 2f, box.height / 2f)
                    val held = offset + (centroid - middle - offset) * (1f - next / scale)
                    offset = clamp(held + pan, next)
                    scale = next
                }
            },
    ) {
        AsyncImage(
            model = file,
            contentDescription = name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offset.x, translationY = offset.y),
        )
    }
}

/**
 * A PDF as a scrolling column of pages, pinched to zoom.
 *
 * The column under the fingers scrolls on a one-finger drag, and a pinch
 * starts as one: the two are the same gesture until the second finger lands.
 * So [detectPinch] claims nothing until it does — see there for why the
 * events are then taken before the scroll sees them.
 *
 * A zoom here changes the layout rather than stretching a picture, which is
 * what keeps the print sharp and what makes the arithmetic necessary: the
 * pages are re-measured around the column's own top left, so unless the
 * scroll is moved to match, the page crawls up and to the left away from the
 * fingers that are growing it. [PageRuler] works out where to move it to, and
 * the one horizontal scroll shared by every page — rather than one each —
 * is what lets the sideways half of that be a single number.
 */
@Composable
private fun PdfPages(file: File, onPage: (Int, Int) -> Unit) {
    val source = remember(file) { PdfSource.open(file) }
    DisposableEffect(source) { onDispose { source?.close() } }
    if (source == null) {
        Message("This PDF cannot be opened here. It may be damaged, or locked with a password.")
        return
    }
    // Animatable rather than a plain float, so a pinch can snap straight to
    // the fingers while a double tap glides: both move the same value, and
    // whichever gesture is in hand cancels the other's animation.
    val zoom = remember { Animatable(1f) }
    // Whether the zoom is the person's or still the one the screen chose, so
    // that turning the tablet re-fits a page nobody has touched and leaves
    // alone one somebody has.
    var touched by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val sideways = rememberScrollState()
    val density = LocalDensity.current
    // The page the column has settled on, for the line under the file's name.
    LaunchedEffect(listState, source) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { onPage(it + 1, source.pageCount) }
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val frameWidth = with(density) { maxWidth.roundToPx() }
        val topPad = 72.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        // The frame the page has to fit in is what is left under the chrome,
        // not the window: fitted to the window, the last inch of the page
        // would be behind the file's name.
        val frameHeight = with(density) { (maxHeight - topPad).roundToPx() }
        val gap = with(density) { Spacing.sm.roundToPx() }
        val ruler = remember(source, frameWidth, gap) { PageRuler(source.sizes, frameWidth, gap) }
        // 1x is the page fitted to the width, which is the right place to
        // start and the wrong place to stop: an A4 sheet across a tablet held
        // sideways is half as tall again as the screen, so a quarter of a page
        // is all there is to see and nothing could be done about it. The floor
        // is the whole first page in the frame, so a pinch inwards reaches it.
        val minZoom = fitPage(source.sizes.firstOrNull(), frameWidth, frameHeight)
        LaunchedEffect(minZoom) {
            if (touched) zoom.snapTo(zoom.value.coerceIn(minZoom, MAX_PDF_ZOOM)) else zoom.snapTo(minZoom)
        }

        /**
         * Moves the zoom to [to] while holding [focus] — a point on the
         * screen, in pixels from its top left — over the same place on the
         * page. The scroll is moved in the same frame as the zoom, so the two
         * never disagree for long enough to be seen as a jump.
         */
        fun zoomAround(to: Float, focus: Offset, snap: Boolean) {
            val from = zoom.value
            val next = to.coerceIn(minZoom, MAX_PDF_ZOOM)
            if (next == from) return
            touched = true
            // Sideways is the easy half: the column is one scroller, so the
            // point under the finger is (scroll + finger) into it, and after
            // the zoom it wants to be that much further in again.
            val acrossBefore = sideways.value + focus.x
            val (page, within) = ruler.anchor(
                listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset,
                focus.y, from, next,
            )
            scope.launch {
                if (snap) zoom.snapTo(next) else zoom.animateTo(next, tween(220))
                listState.scrollToItem(page, within)
                sideways.scrollTo((acrossBefore * next / from - focus.x).roundToInt().coerceAtLeast(0))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(minZoom) {
                    detectTapGestures(
                        // Stepped about the point that was tapped, the same as
                        // the pinch, so the two gestures do not disagree about
                        // what the screen is centred on.
                        onDoubleTap = { at ->
                            val next = if (zoom.value >= MAX_PDF_ZOOM - 0.01f) minZoom
                            else floor(zoom.value + 1f).coerceIn(minZoom, MAX_PDF_ZOOM)
                            zoomAround(next, at, snap = false)
                        },
                    )
                }
                .pinchable { factor, centroid -> zoomAround(zoom.value * factor, centroid, snap = true) },
        ) {
            val columnWidth = with(density) { maxOf((frameWidth * zoom.value).roundToInt(), frameWidth).toDp() }
            Box(modifier = Modifier.fillMaxSize().horizontalScroll(sideways)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.width(columnWidth).fillMaxHeight(),
                    // Clear of the chrome at both ends: the first page starts
                    // below the file's name, and the last can be scrolled past
                    // the arrows.
                    contentPadding = PaddingValues(top = topPad, bottom = Spacing.xl * 3),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    items(source.pageCount) { index ->
                        PdfPage(source, index, frameWidth, zoom.value)
                    }
                }
            }
            // Only once it is the person's zoom: the fitted page a landscape
            // tablet opens on is not something they chose, so it is not news.
            if (touched && abs(zoom.value - 1f) > 0.01f) Text(
                oneDecimal(zoom.value) + "\u00d7",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.md)
                    .clip(CircleShape)
                    .background(PILL)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
    }
}

/**
 * A two-finger pinch, and only that.
 *
 * Nothing is claimed on the first finger, so the column still scrolls. Once a
 * second one is down the events are consumed in the Initial pass — before the
 * scroll sees them at all — because a scroll reading the midpoint of two
 * fingers walks the page up the screen while the pinch is growing it, and the
 * page lands somewhere neither gesture asked for. It stays claimed until every
 * finger is up: the one left behind at the end of a pinch is a leftover, not
 * the start of a scroll.
 *
 * The centroid goes out with the factor, because a zoom that does not know
 * where the fingers are can only grow the page from a corner.
 *
 * [pinchable] is the same thing as a modifier, which is how it is put on
 * something a test can pinch: the pages themselves need a real renderer, and
 * this half of the screen is the half a press can get wrong.
 */
internal fun Modifier.pinchable(onPinch: (Float, Offset) -> Unit) = pointerInput(Unit) { detectPinch(onPinch) }

private suspend fun PointerInputScope.detectPinch(onPinch: (Float, Offset) -> Unit) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
        var pinching = false
        var event: PointerEvent
        do {
            event = awaitPointerEvent(PointerEventPass.Initial)
            if (event.changes.count { it.pressed } >= 2) {
                pinching = true
                val factor = event.calculateZoom()
                if (factor != 1f) onPinch(factor, event.calculateCentroid(useCurrent = true))
            }
            if (pinching) event.changes.forEach { it.consume() }
        } while (event.changes.any { it.pressed })
    }
}

@Composable
private fun PdfPage(source: PdfSource, index: Int, frameWidth: Int, scale: Float) {
    val density = LocalDensity.current
    val size = source.sizes.getOrNull(index) ?: IntSize(1, 1)
    val widthPx = (frameWidth * scale).roundToInt().coerceAtLeast(1)
    // The bitmap is remembered against the page and the width it was drawn
    // for, so a page scrolled past is freed and a zoom redraws it sharp. That
    // width is stepped rather than followed: a pinch changes the layout every
    // frame, and a page re-rendered at each of those widths would stall the
    // gesture it is meant to answer. Between steps the step itself is
    // stretched, and a quarter of a step is not something an eye picks up.
    val render = renderWidthFor(frameWidth * stepped(scale), size)
    val bitmap by produceState<Bitmap?>(null, index, render) {
        value = source.render(index, render)
    }
    val pageWidth = with(density) { widthPx.toDp() }
    val pageHeight = with(density) { (widthPx.toFloat() * size.height / size.width).toDp() }
    Box(
        // Centred, because below 1x the page is narrower than the column and a
        // sheet of paper pinned to the left edge of a dark screen reads as a
        // layout that broke rather than one that zoomed out.
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .width(pageWidth)
                .height(pageHeight)
                // Paper, under the pixels and before them: a page that arrives
                // into a dark gap flashes, and the gap is the wrong colour for
                // a document's own margins anyway.
                .background(Color.White),
        ) {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Page ${index + 1}",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun Downloading(fraction: Float, name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxSize().padding(Spacing.xl),
    ) {
        Spacer(Modifier.weight(1f))
        if (fraction <= 0f) CircularProgressIndicator(color = Color.White)
        else LinearProgressIndicator(
            progress = { fraction },
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.6f).testTag(VIEWER_DOWNLOAD_BAR),
        )
        Text(
            if (name.isBlank()) "Fetching…" else "Fetching $name…",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun Message(text: String) {
    Box(modifier = Modifier.fillMaxSize().padding(Spacing.xl), contentAlignment = Alignment.Center) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
        )
    }
}

/** An icon-only control over the picture: 40dp, white, labelled for a reader. */
@Composable
private fun RoundAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) {
        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(22.dp))
    }
}

/** Near-black rather than black: a photograph's own black edge still shows. */
private val SURROUND = Color(0xFF0B0B0C)
private val SCRIM = Color(0xFF000000).copy(alpha = 0.55f)

/** Darker than the top strip: the arrows float over a page of white paper. */
private val PILL = Color(0xFF000000).copy(alpha = 0.82f)

private const val DOUBLE_TAP_SCALE = 2.5f
private const val MAX_SCALE = 6f
private const val MAX_PDF_ZOOM = 3f

const val FILE_VIEWER_SCREEN = "file-viewer-screen"
const val VIEWER_DOWNLOAD_BAR = "file-viewer-download"

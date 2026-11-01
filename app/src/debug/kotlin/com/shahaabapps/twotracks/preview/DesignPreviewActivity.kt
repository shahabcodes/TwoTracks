package com.shahaabapps.twotracks.preview

import com.shahaabapps.twotracks.feature.settings.WidgetsContent
import com.shahaabapps.twotracks.changelog.ChangelogContent
import com.shahaabapps.twotracks.feature.settings.AvailabilityContent
import com.shahaabapps.twotracks.feature.settings.SettingsContent
import com.shahaabapps.twotracks.feature.budget.BudgetContent
import com.shahaabapps.twotracks.feature.documents.DocumentsContent
import com.shahaabapps.twotracks.feature.tests.TestsContent
import com.shahaabapps.twotracks.feature.countries.CountryContent
import com.shahaabapps.twotracks.feature.countries.CountriesContent
import com.shahaabapps.twotracks.feature.partner.PartnerContent
import com.shahaabapps.twotracks.feature.us.UsContent
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHostState
import com.shahaabapps.twotracks.feature.study.FitContent
import com.shahaabapps.twotracks.feature.study.GoalEditContent
import com.shahaabapps.twotracks.feature.study.GoalDetailContent
import com.shahaabapps.twotracks.feature.levels.LevelContent
import com.shahaabapps.twotracks.feature.levels.LevelsContent
import com.shahaabapps.twotracks.feature.learn.DeckContent
import com.shahaabapps.twotracks.feature.learn.QuizContent
import com.shahaabapps.twotracks.feature.learn.TopicContent
import com.shahaabapps.twotracks.feature.learn.CourseContent
import com.shahaabapps.twotracks.feature.learn.LearnContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shahaabapps.core.model.AvailabilityBlock
import com.shahaabapps.core.model.BlockKind
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.LevelItem
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.model.PlannedSession
import com.shahaabapps.core.model.Profile
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.planner.CapacityReport
import com.shahaabapps.core.planner.LevelItemProgress
import com.shahaabapps.core.planner.LevelProgress
import com.shahaabapps.core.planner.LevelState
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.feature.today.NextTopic
import com.shahaabapps.twotracks.feature.today.PersonCard
import com.shahaabapps.twotracks.feature.today.SessionRow
import com.shahaabapps.twotracks.feature.today.TodayContent
import com.shahaabapps.twotracks.feature.today.TodayUiState
import com.shahaabapps.twotracks.feature.week.BlockRow
import com.shahaabapps.twotracks.feature.week.BothWeeksContent
import com.shahaabapps.twotracks.feature.week.BothWeeksUiState
import com.shahaabapps.twotracks.feature.week.DayPlan
import com.shahaabapps.twotracks.feature.week.PersonWeek
import com.shahaabapps.twotracks.feature.week.WeekContent
import com.shahaabapps.twotracks.feature.week.WeekUiState
import java.time.LocalDate

/**
 * Debug builds only: the redesigned screens drawn from the prototype's sample
 * data, with no account and no Firestore, so they can be put next to the
 * prototype and compared.
 *
 *   adb shell am start -n com.shahaabapps.twotracks/.preview.DesignPreviewActivity --es screen today [--ez dark true] [--es look departures]
 *
 * Screens: today, week, both, learn, course, topic, quiz, results, deck, levels, level, goal, goaledit, fit, us, partner, countries, country, tests, documents, budget, settings, hours, whatsnew, widgets.
 * The frame itself — the tabs or, on a wide window, the rail, with the playing bar: shell (--es tab learn|us).
 * Departures only (with --es look departures): signin, signup, reset, role, space, join, lock.
 * The tour over its screen at a step: guide, with --ei step N.
 * An attachment in the viewer: viewer (--es kind pdf for the PDF rather than the picture); removefile for its delete question.
 * The podcast: listen (--ez transcript true for Parallel's transcript, --ez paused true, --ei line N for the board at that line, --es topic ID for another episode's board, --ez cover true for the cover instead), nowplaying (the bar over Learn), aztopic (a topic with its Listen card), live (the real player and its notification; --es topic ID).
 * Both launcher icons, as a launcher crops them: icons. --es icon departures (or parallel) runs the real icon switch;
 * --es setlook departures saves the look and closes, for the whole path through the app itself.
 */
@dagger.hilt.android.AndroidEntryPoint
class DesignPreviewActivity : ComponentActivity() {
    /** Where the live player's sound comes from: the couple space's storage, as in the app. */
    @javax.inject.Inject lateinit var audio: com.shahaabapps.twotracks.feature.listen.EpisodeAudio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val screen = intent?.getStringExtra("screen") ?: "today"
        val dark = intent?.getBooleanExtra("dark", false) == true
        val look = if (intent?.getStringExtra("look") == "departures") AppLook.DEPARTURES else AppLook.PARALLEL
        // --es setlook departures|parallel saves the look as Settings does, so the app opened
        // afterwards from its icon runs the whole path: the saved look, then the icon as it leaves.
        intent?.getStringExtra("setlook")?.let { name ->
            kotlinx.coroutines.runBlocking { com.shahaabapps.core.common.datastore.ThemePreferences(applicationContext).setLookName(name.uppercase()) }
            finish()
            return
        }
        // --es icon departures|parallel runs the real icon switch, as the app does when it leaves the screen.
        intent?.getStringExtra("icon")?.let { com.shahaabapps.twotracks.LauncherIcon.apply(this, if (it == "departures") AppLook.DEPARTURES else AppLook.PARALLEL) }
        setContent {
            AppTheme(themeMode = if (dark) ThemeMode.DARK else ThemeMode.LIGHT, look = look) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // The app draws every screen inside ReadableColumn, so the
                    // preview must too, or it would show a layout the app does
                    // not have. The same screens opt out here as there.
                    com.shahaabapps.twotracks.ui.ReadableColumn(wide = screen in setOf("listen", "live", "shell", "viewer")) {
                    when (screen) {
                        // The whole frame — tabs or rail, the playing bar, and a
                        // screen inside it — so the wide layout can be seen
                        // without an account. --es tab learn|us picks the tab.
                        "shell" -> com.shahaabapps.twotracks.ui.AppShell(
                            playing = ListenSamples.nowPlaying(),
                            selectedTab = intent?.getStringExtra("tab") ?: "today",
                            onOpen = {}, onPlayPause = {}, onForward30 = {}, onTab = {},
                        ) { padding ->
                            androidx.compose.foundation.layout.Box(Modifier.padding(padding)) {
                                com.shahaabapps.twotracks.ui.ReadableColumn {
                                    TodayContent(
                                        state = Samples.today(), onOpenLevel = {}, onOpenPartner = {}, onOpenGoal = {}, onOpenWeek = {},
                                        onOpenSettings = {}, onOpenChangelog = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> },
                                        onOpenBrief = {}, onDismissBrief = {}, onDismissWhatsNew = {}, onDone = {}, onSkip = {}, onReopen = {}, onMessageShown = {},
                                    )
                                }
                            }
                        }
                        "week" -> WeekContent(
                            state = Samples.week(), onBack = {}, onOpenGoal = {}, onOpenBothWeeks = {}, onOpenFit = {},
                            onPreviousWeek = {}, onNextWeek = {}, onThisWeek = {}, onTogglePerson = {}, onReplan = {},
                            onAssign = { _, _, _ -> }, onAddBlock = { _, _, _ -> }, onMessageShown = {},
                        )
                        "both" -> BothWeeksContent(state = Samples.both(), onBack = {})
                        "learn" -> LearnContent(LearnSamples.learn(), onOpenCourse = {}, onOpenTopic = { _, _ -> }, onOpenDeck = {}, onOpenQuiz = { _, _ -> })
                        "course" -> CourseContent(LearnSamples.germanId, LearnSamples.learn(), Samples.today, onBack = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> })
                        "topic" -> TopicContent(
                            LearnSamples.topic(), speaker = null, voiceMissing = false, recognizer = null, onBack = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> },
                            onInstallVoice = {}, onChecked = { _, _, _, _ -> }, onConfidence = { _, _ -> }, onMarkLearned = {}, onUnlearn = {}, onMessageShown = {}, onListen = {},
                        )
                        // The files kept against a module: --es screen files
                        "files" -> com.shahaabapps.twotracks.feature.learn.ModuleFilesContent(
                            com.shahaabapps.twotracks.feature.learn.ModuleFilesUiState(
                                courseTitle = "AZ-204 Azure Developer", moduleTitle = "Compute", loaded = true,
                                files = listOf(
                                    com.shahaabapps.core.model.ModuleFile(
                                        id = "f1", courseId = "az-204", moduleId = "az-compute", name = "exam-guide.pdf",
                                        mimeType = "application/pdf", sizeBytes = 2_400_000, uploadedAtIso = "2026-11-01T09:00:00Z",
                                    ),
                                    com.shahaabapps.core.model.ModuleFile(
                                        id = "f2", courseId = "az-204", moduleId = "az-compute", name = "whiteboard.jpg",
                                        mimeType = "image/jpeg", sizeBytes = 812_000, uploadedAtIso = "2026-10-30T18:20:00Z",
                                    ),
                                ),
                            ),
                            onBack = {}, onAdd = {}, onOpen = {}, onDelete = {},
                        )
                        // The viewer, over a real PNG or a real multi-page PDF:
                        //   --es screen viewer [--es kind pdf]
                        "viewer" -> {
                            val pdf = intent?.getStringExtra("kind") == "pdf"
                            val picture = com.shahaabapps.core.model.ModuleFile(
                                id = "f2", courseId = "az-204", moduleId = "az-compute", name = "whiteboard.png",
                                mimeType = "image/png", sizeBytes = 812_000, uploadedAtIso = "2026-10-30T18:20:00Z",
                            )
                            val guide = com.shahaabapps.core.model.ModuleFile(
                                id = "f1", courseId = "az-204", moduleId = "az-compute", name = "exam-guide.pdf",
                                mimeType = "application/pdf", sizeBytes = 2_400_000, uploadedAtIso = "2026-11-01T09:00:00Z",
                            )
                            com.shahaabapps.twotracks.feature.learn.FileViewerContent(
                                state = com.shahaabapps.twotracks.feature.learn.FileViewerUiState(
                                    file = if (pdf) guide else picture,
                                    siblings = listOf(guide, picture),
                                    local = if (pdf) SampleFiles.pdf(this) else SampleFiles.image(this),
                                ),
                                onBack = {}, onPrevious = {}, onNext = {}, onOpenElsewhere = {}, onDelete = {},
                            )
                        }
                        // The question asked before an attachment is removed:
                        //   --es screen removefile
                        // The rename question over the list it is asked from:
                        //   --es screen renamefile
                        "renamefile" -> {
                            val scan = com.shahaabapps.core.model.ModuleFile(
                                id = "f3", courseId = "az-204", moduleId = "az-compute", topicId = "az-app-service",
                                name = "IMG_20261101_094312.jpg", mimeType = "image/jpeg", sizeBytes = 1_240_000,
                                uploadedAtIso = "2026-11-01T09:43:00Z",
                            )
                            com.shahaabapps.twotracks.feature.learn.ModuleFilesContent(
                                state = com.shahaabapps.twotracks.feature.learn.ModuleFilesUiState(
                                    courseTitle = "AZ-204", moduleTitle = "Compute", topicTitle = "App Service",
                                    files = listOf(scan), loaded = true,
                                ),
                                onBack = {}, onAdd = {}, onOpen = {}, onDelete = {},
                            )
                            com.shahaabapps.twotracks.feature.learn.RenameFileDialog(
                                file = scan, onConfirm = {}, onDismiss = {},
                            )
                        }
                        "removefile" -> {
                            val picture = com.shahaabapps.core.model.ModuleFile(
                                id = "f2", courseId = "az-204", moduleId = "az-compute", name = "whiteboard.png",
                                mimeType = "image/png", sizeBytes = 812_000, uploadedAtIso = "2026-10-30T18:20:00Z",
                            )
                            com.shahaabapps.twotracks.feature.learn.FileViewerContent(
                                state = com.shahaabapps.twotracks.feature.learn.FileViewerUiState(
                                    file = picture, siblings = listOf(picture), local = SampleFiles.image(this),
                                ),
                                onBack = {}, onPrevious = {}, onNext = {}, onOpenElsewhere = {}, onDelete = {},
                            )
                            com.shahaabapps.twotracks.feature.learn.RemoveFileDialog(
                                file = picture, preview = SampleFiles.image(this), onConfirm = {}, onDismiss = {},
                            )
                        }
                        "aztopic" -> TopicContent(
                            ListenSamples.topic(), speaker = null, voiceMissing = false, recognizer = null, onBack = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> },
                            onInstallVoice = {}, onChecked = { _, _, _, _ -> }, onConfidence = { _, _ -> }, onMarkLearned = {}, onUnlearn = {}, onMessageShown = {}, onListen = {},
                        )
                        // A notes sheet: --es screen notes --es topic ai-container-apps
                        "notes" -> {
                            val id = intent?.getStringExtra("topic") ?: ListenSamples.TOPIC
                            val course = com.shahaabapps.core.content.Courses.all.firstOrNull { c -> c.allTopics.any { it.id == id } }
                            com.shahaabapps.twotracks.feature.learn.NotesContent(
                                notes = com.shahaabapps.core.content.Notes.forTopic(id),
                                topicTitle = course?.allTopics?.firstOrNull { it.id == id }?.title,
                                courseTitle = course?.title,
                                onBack = {},
                            )
                        }
                        // The real timing from the app's assets, so the transcript lights the line actually spoken there.
                        "listen" -> com.shahaabapps.twotracks.feature.listen.ListenContent(
                            intent?.getStringExtra("topic")?.let { id ->
                                ListenSamples.listenTo(id, com.shahaabapps.twotracks.feature.listen.Episodes(applicationContext).timeline(id)!!, intent?.getIntExtra("line", 6) ?: 6)
                            } ?: ListenSamples.listen(
                                com.shahaabapps.twotracks.feature.listen.Episodes(applicationContext).timeline(ListenSamples.TOPIC),
                                playing = intent?.getBooleanExtra("paused", false) != true,
                                line = intent?.getIntExtra("line", 6) ?: 6,
                                board = intent?.getBooleanExtra("cover", false) != true,
                            ).let { state ->
                                // --ef fetch 0.4 draws the play button partway through a download.
                                val f = intent?.getFloatExtra("fetch", -1f) ?: -1f
                                if (f < 0f) state else state.copy(fetch = com.shahaabapps.twotracks.feature.listen.EpisodeAudio.Fetch.Downloading(f), playing = false)
                            },
                            onBack = {}, onPlayPause = {}, onBack15 = {}, onForward30 = {}, onSeek = {}, onSpeed = {}, onStop = {}, onLine = {},
                            transcriptFirst = intent?.getBooleanExtra("transcript", false) == true,
                            // --ez hidden true draws the controls folded away.
                            controlsHidden = intent?.getBooleanExtra("hidden", false) == true,
                        )
                        // The real player, not a picture of it: the service, the notification and the
                        // lock screen, for an episode the app carries. --es topic az-apim picks another.
                        "live" -> {
                            val vm = remember {
                                val episodes = com.shahaabapps.twotracks.feature.listen.Episodes(applicationContext)
                                val place = com.shahaabapps.twotracks.feature.listen.ListenPlace(applicationContext)
                                com.shahaabapps.twotracks.feature.listen.ListenViewModel(
                                    (intent?.getStringExtra("topic") ?: ListenSamples.TOPIC).let { episode ->
                                        // The course the episode's topic is in, so a German episode plays as one.
                                        val course = com.shahaabapps.twotracks.feature.listen.Episodes.courseOf(episode)!!
                                        androidx.lifecycle.SavedStateHandle(mapOf("courseId" to course, "topicId" to episode))
                                    },
                                    episodes, com.shahaabapps.twotracks.feature.listen.ListenPlayer(applicationContext, episodes, place, audio), place, audio,
                                )
                            }
                            com.shahaabapps.twotracks.feature.listen.ListenScreen(onBack = { finish() }, viewModel = vm)
                        }
                        "nowplaying" -> androidx.compose.foundation.layout.Box(Modifier.fillMaxSize()) {
                            LearnContent(LearnSamples.learn(), onOpenCourse = {}, onOpenTopic = { _, _ -> }, onOpenDeck = {}, onOpenQuiz = { _, _ -> })
                            com.shahaabapps.twotracks.feature.listen.NowPlayingBar(
                                ListenSamples.nowPlaying(), onOpen = {}, onPlayPause = {}, onForward30 = {},
                                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter).navigationBarsPadding(),
                            )
                        }
                        "quiz" -> QuizContent(LearnSamples.quiz(), speaker = null, onBack = {}, onOpenTopic = { _, _ -> }, onToggle = {}, onCheck = {}, onNext = {}, onRetake = {})
                        "results" -> QuizContent(LearnSamples.results(), speaker = null, onBack = {}, onOpenTopic = { _, _ -> }, onToggle = {}, onCheck = {}, onNext = {}, onRetake = {})
                        "levels" -> LevelsContent(PlanSamples.levels(), onBack = {}, onOpenLevel = {}, onShowPartner = {})
                        "level" -> LevelContent(
                            PlanSamples.level(), onBack = {}, onOpenGoal = {}, onNewGoal = {}, onToggleTask = {}, onAddTask = { _, _, _ -> }, onRemove = {},
                            onStartNext = {}, onChooseBranch = {}, onMessageShown = {},
                        )
                        "goal" -> GoalDetailContent(
                            PlanSamples.goal(), remember { SnackbarHostState() }, onBack = {}, onEdit = {}, onOpenCourse = {},
                            onSetPassed = {}, onAddMock = { _, _ -> }, onDeleteMock = {}, onDeleteLog = {},
                        )
                        "goaledit" -> GoalEditContent(
                            PlanSamples.goalEdit(), onCancel = {}, onTitle = {}, onWhy = {}, onKind = {}, onTargetDate = {}, onPriority = {}, onRequiredForLevel = {},
                            onComponent = { _, _, _, _ -> }, onAddComponent = {}, onRemoveComponent = {}, onPreset = { _, _ -> }, onSave = {}, onDelete = {},
                        )
                        "fit" -> com.shahaabapps.twotracks.feature.study.FitSheet(PlanSamples.fit(), onChoose = {}, onDismiss = {})
                        "us" -> UsContent(TogetherSamples.us(), onNavigate = {}, onOpenLevel = {})
                        "partner" -> PartnerContent(TogetherSamples.partner(), onBack = {}, onOpenLevel = {}, onOpenGoal = {})
                        "countries" -> CountriesContent(TogetherSamples.countries(), onBack = {}, onOpen = {})
                        "country" -> CountryContent(TogetherSamples.country(), onBack = {}, onToggle = { _, _ -> }, onSaveNotes = { _, _ -> }, onClearNotes = {})
                        "tests" -> TestsContent(TogetherSamples.tests(), onBack = {}, onSave = {})
                        "documents" -> DocumentsContent(TogetherSamples.documents(), onBack = {}, onFilter = {}, onCycle = {}, onSave = {})
                        "budget" -> BudgetContent(TogetherSamples.budget(), onBack = {}, onSave = {}, onDelete = {})
                        "settings" -> SettingsContent(
                            SettingsSamples.settings(), remember { SnackbarHostState() }, onBack = {}, onEditProfile = {}, onCopyInvite = {}, onOpenAvailability = {},
                            onEditStartDate = {}, onOpenFullScreen = {}, onTheme = {}, onLook = {}, onBiometric = {}, onOpenVoices = {}, onOpenWidgets = {}, onOpenChangelog = {},
                            onTour = {}, onReset = {}, onSignOut = {},
                        )
                        "hours" -> AvailabilityContent(SettingsSamples.blocks, isTech = true, goals = emptyList(), onBack = {}, onAdd = {}, onRemove = {})
                        "whatsnew" -> ChangelogContent(SettingsSamples.releases(), "0.76.0", onBack = {})
                        "widgets" -> WidgetsContent(
                            listOf(
                                com.shahaabapps.twotracks.widget.PlacedWidget(1, com.shahaabapps.twotracks.widget.WidgetConfig()),
                                com.shahaabapps.twotracks.widget.PlacedWidget(2, com.shahaabapps.twotracks.widget.WidgetConfig(content = com.shahaabapps.twotracks.widget.WidgetContent.WORD_OF_DAY, courseId = "de-a1", theme = com.shahaabapps.twotracks.widget.WidgetTheme.DARK)),
                            ),
                            emptyMap(), onBack = {}, onChange = {},
                        )
                        "signin", "signup", "reset", "role", "space", "join", "lock" -> BoardAccountPreview(screen)
                        "brief" -> com.shahaabapps.twotracks.feature.today.DailyBriefSheet(sampleBrief(), AppTheme.colors.tech, onStart = {}, onDismiss = {})
                        "icons" -> IconsPreview()
                        "guide" -> GuidePreview(intent?.getIntExtra("step", 1) ?: 1)
                        "deck" -> DeckContent(LearnSamples.deck(), speaker = null, onBack = {}, onFlip = {}, onAnswer = {}, onPractiseAnyway = {}, onAgain = {}, onInstallVoice = {})
                        else -> TodayContent(
                            state = Samples.today(), onOpenLevel = {}, onOpenPartner = {}, onOpenGoal = {}, onOpenWeek = {},
                            onOpenSettings = {}, onOpenChangelog = {}, onOpenTopic = { _, _ -> }, onOpenQuiz = { _, _ -> },
                            onOpenBrief = {}, onDismissBrief = {}, onDismissWhatsNew = {}, onDone = {}, onSkip = {}, onReopen = {}, onMessageShown = {},
                        )
                    }
                    }
                }
            }
        }
    }
}

/** The prototype's Monday, 12 October 2026, for Usaid in Doha and Sanya in Karachi. */
internal object Samples {
    val today: LocalDate = LocalDate.of(2026, 10, 12)
    private val usaid = Profile(uid = "u", name = "Usaid", role = PersonRole.TECH, timezone = "Asia/Qatar")
    private val sanya = Profile(uid = "s", name = "Sanya", role = PersonRole.TEACHER, timezone = "Asia/Karachi")
    private val az = StudyGoal(id = "az", owner = PersonRole.TECH, title = "AZ-204")
    private val deU = StudyGoal(id = "de-u", owner = PersonRole.TECH, title = "German A1")
    private val ielts = StudyGoal(id = "ielts", owner = PersonRole.TEACHER, title = "IELTS")
    private val deS = StudyGoal(id = "de-s", owner = PersonRole.TEACHER, title = "German A1")

    private fun row(id: String, owner: PersonRole, goal: StudyGoal, component: String, start: Int, minutes: Int, status: SessionStatus = SessionStatus.PLANNED, reader: Int = start, isClass: Boolean = false, topic: NextTopic? = null) =
        SessionRow(
            session = PlannedSession(id = id, owner = owner, dateIso = today.toString(), startMinutes = start, endMinutes = start + minutes, status = status, isClass = isClass),
            goal = goal, componentName = component,
            timeLabel = "%02d:%02d to %02d:%02d".format(start / 60, start % 60, (start + minutes) / 60, (start + minutes) % 60),
            nextTopic = topic, readerStartMinutes = reader,
        )

    private fun level(name: String, done: Int, total: Int, due: String) = LevelProgress(
        level = Level(id = name, number = 1, name = name, dueDateIso = due),
        state = LevelState.OPEN,
        items = (0 until total).map { LevelItemProgress(LevelItem(id = "i$it", required = true), done = it < done, fraction = null, weekCount = null) },
        today = today,
    )

    fun today() = TodayUiState(
        loading = false,
        today = today,
        level = level("Foundations", 2, 5, "2027-03-31"),
        partnerLevel = level("Build the base", 1, 3, "2026-12-31"),
        me = PersonCard(
            role = PersonRole.TECH, name = "Usaid", profile = usaid, todayMinutes = 20, streak = 6, localTime = "09:12", isRestDay = false,
            sessions = listOf(
                row("u1", PersonRole.TECH, deU, "Words", 7 * 60, 20, SessionStatus.DONE),
                row("u2", PersonRole.TECH, az, "Compute", 19 * 60, 60, topic = NextTopic("az204", "app-service", "App Service", 28)),
            ),
        ),
        partner = PersonCard(
            role = PersonRole.TEACHER, name = "Sanya", profile = sanya, todayMinutes = 0, streak = 6, localTime = "11:12", isRestDay = false,
            sessions = listOf(
                row("s1", PersonRole.TEACHER, ielts, "Listening class", 16 * 60, 90, reader = 14 * 60, isClass = true),
                row("s2", PersonRole.TEACHER, deS, "Grammar", 23 * 60 + 30, 30, reader = 21 * 60 + 30),
            ),
        ),
        togetherStreak = 6,
    )

    fun week(): WeekUiState {
        val start = today
        fun block(day: Int, from: Int, to: Int, kind: BlockKind = BlockKind.STUDY) = AvailabilityBlock(id = "$day-$from", dayOfWeek = day, startMinutes = from, endMinutes = to, kind = kind)
        fun study(day: Int, from: Int, minutes: Int, goal: StudyGoal, component: String, status: SessionStatus = SessionStatus.PLANNED) =
            BlockRow(block(day, from, from + minutes), PlannedSession(id = "$day-$from", startMinutes = from, endMinutes = from + minutes, status = status), goal, component)
        val work = BlockRow(block(0, 8 * 60, 17 * 60, BlockKind.WORK), null, null, "")
        val days = listOf(
            listOf(study(1, 7 * 60, 20, deU, "Words", SessionStatus.DONE), work, study(1, 19 * 60, 60, az, "App Service")),
            listOf(work, study(2, 19 * 60, 60, az, "Deployment slots")),
            listOf(study(3, 7 * 60, 30, deU, "Grammar"), work, study(3, 19 * 60, 60, az, "Functions")),
            listOf(work, study(4, 19 * 60, 90, az, "Containers")),
            emptyList(),
            listOf(study(6, 10 * 60, 120, az, "Module 1 test"), study(6, 16 * 60, 45, deU, "Dialogue")),
            listOf(study(7, 7 * 60, 20, deU, "Words"), work, study(7, 19 * 60, 60, az, "Blob storage")),
        ).mapIndexed { i, blocks -> DayPlan(date = start.plusDays(i.toLong()), isToday = i == 0, blocks = blocks) }
        return WeekUiState(
            weekStart = start, days = days, viewingRole = PersonRole.TECH, viewingName = "Usaid", isMe = true,
            capacity = CapacityReport(weeklyCapacityHours = 9.4, weeklyNeedHours = 8.0, perGoalNeed = emptyList()),
            plannedMinutes = 564, doneMinutes = 20,
        )
    }

    fun both() = BothWeeksUiState(
        days = (6 downTo 0).map { today.minusDays(it.toLong()) },
        me = PersonWeek(PersonRole.TECH, "Usaid", 505, "AZ-204 5.5 · German A1 2.9", listOf(75, 60, 90, 30, 150, 80, 20)),
        partner = PersonWeek(PersonRole.TEACHER, "Sanya", 415, "IELTS 5.4 · German A1 1.5", listOf(120, 45, 120, 40, 60, 30, 0)),
        togetherStreak = 6,
        partnerName = "Sanya",
    )
}

package com.shahaabapps.twotracks.ui

import com.shahaabapps.twotracks.feature.learn.DeckScreen
import com.shahaabapps.twotracks.feature.learn.CourseScreen
import com.shahaabapps.twotracks.feature.learn.LearnScreen
import com.shahaabapps.twotracks.feature.learn.ModuleFilesScreen
import com.shahaabapps.twotracks.feature.learn.NotesScreen
import com.shahaabapps.twotracks.feature.learn.QuizScreen
import com.shahaabapps.twotracks.feature.learn.TopicScreen
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import com.shahaabapps.core.content.Courses
import com.shahaabapps.twotracks.feature.listen.ListenScreen
import com.shahaabapps.twotracks.feature.listen.NowPlaying
import com.shahaabapps.twotracks.feature.listen.NowPlayingBar
import com.shahaabapps.twotracks.feature.listen.NowPlayingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.People
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import com.shahaabapps.twotracks.ui.components.BoardIcons
import com.shahaabapps.twotracks.ui.components.ParallelIcons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.GuideViewModel
import com.shahaabapps.twotracks.ui.guide.GuideOverlay
import com.shahaabapps.twotracks.ui.guide.GuideState
import com.shahaabapps.twotracks.ui.guide.LocalGuide
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.twotracks.ui.guide.guideSteps
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.changelog.ChangelogScreen
import com.shahaabapps.twotracks.feature.budget.BudgetScreen
import com.shahaabapps.twotracks.feature.countries.CountriesScreen
import com.shahaabapps.twotracks.feature.countries.CountryDetailScreen
import com.shahaabapps.twotracks.feature.documents.DocumentsScreen
import com.shahaabapps.twotracks.feature.levels.LevelScreen
import com.shahaabapps.twotracks.feature.levels.LevelsScreen
import com.shahaabapps.twotracks.feature.partner.PartnerScreen
import com.shahaabapps.twotracks.feature.settings.AvailabilityScreen
import com.shahaabapps.twotracks.feature.settings.SettingsScreen
import com.shahaabapps.twotracks.feature.settings.VoiceSettingsScreen
import com.shahaabapps.twotracks.feature.settings.WidgetsScreen
import com.shahaabapps.twotracks.feature.study.GoalDetailScreen
import com.shahaabapps.twotracks.feature.study.GoalEditScreen
import com.shahaabapps.twotracks.feature.tests.TestsScreen
import com.shahaabapps.twotracks.feature.today.TodayScreen
import com.shahaabapps.twotracks.feature.us.UsScreen
import com.shahaabapps.twotracks.feature.week.WeekScreen
import com.shahaabapps.twotracks.feature.week.BothWeeksScreen
import com.shahaabapps.twotracks.feature.week.YourWeekScreen
import com.shahaabapps.twotracks.session.AppSession
import com.shahaabapps.twotracks.widget.WidgetLaunch

object Routes {
    const val TODAY = "today"
    const val US = "us"
    const val LEVELS = "levels"
    const val LEARN = "learn"
    const val COURSE = "learn/{courseId}"
    const val TOPIC = "learn/{courseId}/topic/{topicId}"
    const val QUIZ = "learn/{courseId}/quiz/{moduleId}"
    const val MODULE_FILES = "learn/{courseId}/files/{moduleId}"
    const val DECK = "learn/{courseId}/deck"
    const val LISTEN = "learn/{courseId}/listen/{topicId}"
    const val NOTES = "learn/{courseId}/notes/{topicId}"
    const val LEVEL = "level/{levelId}"
    const val YOUR_WEEK = "yourweek"
    const val PARTNER = "partner"
    const val CHANGELOG = "changelog"
    const val WEEK = "week"
    const val BOTH_WEEKS = "week/both"
    const val GOAL_DETAIL = "goal/{goalId}"
    const val GOAL_EDIT = "goal/{goalId}/edit"
    const val GOAL_NEW = "goal/new/edit?levelId={levelId}"
    const val COUNTRIES = "countries"
    const val COUNTRY_DETAIL = "countries/{code}"
    const val TESTS = "tests"
    const val DOCUMENTS = "documents"
    const val BUDGET = "budget"
    const val SETTINGS = "settings"
    const val AVAILABILITY = "settings/availability"
    const val VOICES = "settings/voices"
    const val WIDGETS = "settings/widgets"

    fun level(id: String) = "level/$id"
    fun course(id: String) = "learn/$id"
    fun topic(courseId: String, topicId: String) = "learn/$courseId/topic/$topicId"
    fun quiz(courseId: String, moduleId: String) = "learn/$courseId/quiz/$moduleId"
    fun moduleFiles(courseId: String, moduleId: String) = "learn/$courseId/files/$moduleId"
    fun deck(courseId: String) = "learn/$courseId/deck"
    fun listen(courseId: String, topicId: String) = "learn/$courseId/listen/$topicId"
    fun notes(courseId: String, topicId: String) = "learn/$courseId/notes/$topicId"
    fun goalDetail(id: String) = "goal/$id"
    fun goalEdit(id: String) = "goal/$id/edit"
    fun newGoal(levelId: String? = null) = if (levelId == null) "goal/new/edit" else "goal/new/edit?levelId=$levelId"
    fun country(code: String) = "countries/$code"
}

/**
 * The screens that do want every pixel they are given, so they opt out of
 * [ReadableColumn]'s 720dp cap.
 *
 * Only the podcast, as it turns out. It is the one screen that is a picture
 * rather than a page: a full-bleed navy player with an animated board in the
 * middle of it, and capping it would have left the page colour showing down
 * both sides of a dark screen, which looks like a mistake rather than a
 * margin.
 *
 * The week and both-weeks screens were in here on the assumption that they
 * were grids; looked at on the tablet they are lists of days, and full width
 * stranded each session's length a thousand pixels from its name, so they are
 * better capped like everything else. The wide table in a lesson already
 * scrolls sideways inside its own BoxWithConstraints, so it keeps working
 * whatever width it is given.
 */
private val wideRoutes = setOf(Routes.LISTEN)

/**
 * The window width at which the tabs leave the foot of the screen and become a
 * rail on the leading edge.
 *
 * 840dp is Material's "expanded" break, and on this app it is also where the
 * bottom bar stops being reachable: a 10-inch tablet in landscape is 1280dp
 * across, and the foot of it is nowhere near either hand. A tablet held
 * upright is 800dp, below the break, and keeps the bottom bar — which is
 * right, because upright the foot of the screen is still in reach.
 */
internal const val RAIL_WIDTH_DP = 840

private data class Tab(val route: String, val label: String, val icon: ImageVector)

/**
 * Today, Learn, Together, as the Parallel prototype has them, and the app
 * opens on Today.
 *
 * Learn used to come first, on the argument that the app should open on the
 * work rather than a summary of it. The redesign puts the work on Today: the
 * next session is the first card there, with its topic one tap away, so
 * opening on Today no longer means opening on a report. Levels left the bar
 * for Together, where the rest of the long view already was.
 */
private val tabs = listOf(
    Tab(Routes.TODAY, "Today", ParallelIcons.Tracks),
    Tab(Routes.LEARN, "Learn", Icons.AutoMirrored.Outlined.MenuBook),
    Tab(Routes.US, "Together", Icons.Outlined.People),
)

/**
 * The same three places on the Departures board, as its prototype names them:
 * Today is the board itself, and Together is the route the two of you are on.
 */
private val boardTabs = listOf(
    Tab(Routes.TODAY, "TODAY", BoardIcons.Board),
    Tab(Routes.LEARN, "LEARN", Icons.AutoMirrored.Outlined.MenuBook),
    Tab(Routes.US, "ROUTE", BoardIcons.Route),
)

/** Which tour anchor a tab reports itself as. */
private fun guideAnchorFor(route: String): String = when (route) {
    Routes.LEARN -> GuideAnchors.TAB_LEARN
    Routes.TODAY -> GuideAnchors.TAB_TODAY
    else -> GuideAnchors.TAB_US
}

/**
 * The foot of every screen: the episode you have playing, and under it the
 * tabs when you are on one.
 *
 * The Column is the point of this function. Scaffold measures its bottom bar
 * slot as a multi-child layout and places every child at the same top edge, so
 * two composables written side by side here do not stack, they overlap: the
 * tab bar, drawn second over an opaque background, painted the playing bar out
 * of sight on Today, Learn and Together, while it still showed on every screen
 * that has no tabs. Keeping them in one child also gives Scaffold the real
 * height of the pair, so the screen above reserves room for both.
 */
@Composable
internal fun BottomBars(
    playing: NowPlaying?,
    selectedTab: String?,
    onOpen: (NowPlaying) -> Unit,
    onPlayPause: (NowPlaying) -> Unit,
    onForward30: () -> Unit,
    onTab: (String) -> Unit,
) {
    Column {
        playing?.let { now ->
            NowPlayingBar(
                now,
                onOpen = { onOpen(now) },
                onPlayPause = { onPlayPause(now) },
                onForward30 = onForward30,
                // The tabs below carry the gesture inset when they are there;
                // alone, the bar carries its own.
                modifier = if (selectedTab == null) Modifier.navigationBarsPadding() else Modifier,
            )
        }
        if (selectedTab != null) CupertinoTabBar(selectedRoute = selectedTab, onSelect = onTab)
    }
}

/** The tabs this look names them by: the board writes them in capitals. */
@Composable
private fun currentTabs(): List<Tab> = if (AppTheme.departures) boardTabs else tabs

/**
 * One tab, wherever it is drawn.
 *
 * The bar and the rail share this so they share the selected treatment —
 * ink in Parallel, the board's signal yellow in Departures, and the label in
 * SemiBold — and so the tour finds its anchor either way. Only the box it sits
 * in differs, which is what [modifier] carries.
 */
@Composable
private fun TabItem(tab: Tab, selected: Boolean, modifier: Modifier, onSelect: () -> Unit) {
    val departures = AppTheme.departures
    // The tab you are on is ink in Parallel and the board's signal yellow in Departures.
    val tint = when {
        !selected -> AppTheme.colors.secondaryLabel
        departures -> AppTheme.colors.accent
        else -> AppTheme.colors.ink
    }
    val interaction = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .selectable(selected = selected, role = Role.Tab, interactionSource = interaction, indication = null) { onSelect() }
            .padding(vertical = 6.dp)
            .guideAnchor(guideAnchorFor(tab.route)),
    ) {
        Icon(tab.icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Text(
            tab.label, style = MaterialTheme.typography.labelSmall, color = tint,
            fontWeight = if (selected || departures) FontWeight.SemiBold else FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/** The tab bar: the page colour under a hairline, and the tab you are on in ink, or in signal yellow on the board. */
@Composable
private fun CupertinoTabBar(selectedRoute: String?, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground)) {
        HorizontalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 6.dp).selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            currentTabs().forEach { tab ->
                TabItem(tab, selected = tab.route == selectedRoute, modifier = Modifier.weight(1f)) { onSelect(tab.route) }
            }
        }
        Column(modifier = Modifier.navigationBarsPadding()) {}
    }
}

/**
 * The same three tabs stood on end against the leading edge, for a window wide
 * enough that the foot of the screen is out of reach.
 *
 * It is the tab bar's sibling on purpose: the same tabs in the same order, the
 * same icons and labels, the same selected treatment through [TabItem], and
 * the page colour behind a hairline — here the hairline runs down the trailing
 * edge instead of across the top. The tabs sit in the middle of the rail
 * rather than at its top: on a tablet held in landscape the middle of the
 * leading edge is where the left thumb already is.
 */
@Composable
private fun CupertinoNavRail(selectedRoute: String?, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .background(AppTheme.colors.groupedBackground)
            // The rail owns the leading edge, so it carries the inset on that
            // side — the cutout, or the navigation bar when the device is
            // turned. The padding goes on the Row rather than in with the
            // tabs, so that the inset widens the rail instead of eating it:
            // the tabs keep their full [RailWidth] either way, which they did
            // not when this was one modifier chain and landscape's 70dp start
            // inset left every label about one character wide.
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Start)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(RailWidth)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
                .padding(horizontal = 4.dp)
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            currentTabs().forEach { tab ->
                TabItem(tab, selected = tab.route == selectedRoute, modifier = Modifier.fillMaxWidth()) { onSelect(tab.route) }
            }
        }
        VerticalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
    }
}

private val RailWidth = 84.dp

/**
 * The frame every screen is drawn in: the three tabs, the playing bar, and the
 * window between them.
 *
 * Below [RAIL_WIDTH_DP] this is exactly what it always was — a Scaffold with
 * [BottomBars] at its foot. At or above it the tabs move into
 * [CupertinoNavRail] on the leading edge and the Scaffold moves over to sit
 * beside the rail, which leaves the playing bar alone at the foot of the
 * content, full width of it and clear of the rail: still the last thing above
 * the gesture bar, still one tap, and now plainly the bar for the page you are
 * looking at rather than a second row of chrome under the tabs.
 *
 * It is a composable of its own, rather than a few lines inside [MainNavGraph],
 * so a test can stand it up at a given window size without a NavController,
 * Hilt or an account.
 */
@Composable
internal fun AppShell(
    playing: NowPlaying?,
    selectedTab: String?,
    onOpen: (NowPlaying) -> Unit,
    onPlayPause: (NowPlaying) -> Unit,
    onForward30: () -> Unit,
    onTab: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    // There is nowhere to navigate from a screen that is not a tab, so the
    // rail only appears where the bar would have.
    val rail = LocalConfiguration.current.screenWidthDp >= RAIL_WIDTH_DP && selectedTab != null
    Row(modifier = Modifier.fillMaxSize().background(AppTheme.colors.groupedBackground)) {
        if (rail) CupertinoNavRail(selectedRoute = selectedTab, onSelect = onTab)
        Scaffold(
            containerColor = AppTheme.colors.groupedBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                BottomBars(
                    playing = playing,
                    // With a rail the tabs are already drawn, to the left.
                    // Passing null here is also what tells the playing bar to
                    // carry the gesture inset itself, which it must, because
                    // there is no longer a tab bar under it to do that.
                    selectedTab = selectedTab.takeUnless { rail },
                    onOpen = onOpen,
                    onPlayPause = onPlayPause,
                    onForward30 = onForward30,
                    onTab = onTab,
                )
            },
            content = content,
        )
    }
}

@Composable
fun MainNavGraph(session: AppSession, widgetLaunch: WidgetLaunch) {
    val navController = rememberNavController()

    // A widget tap names where it wants to land. It cannot be honoured until
    // the graph exists, so it waits in WidgetLaunch until here.
    val widgetRoute by widgetLaunch.route.collectAsStateWithLifecycle()
    LaunchedEffect(widgetRoute) {
        val route = widgetRoute ?: return@LaunchedEffect
        widgetLaunch.consume()
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
        }
    }
    // One tour for the whole graph, so a step can point at the tab bar and at
    // a card on a screen without either knowing about the other.
    val guide = remember { GuideState(guideSteps(Routes.LEARN, Routes.TODAY, Routes.LEVELS, Routes.US, Routes.SETTINGS)) }
    // Owed to whoever has just signed in, and cleared however the tour ends.
    val guideViewModel: GuideViewModel = hiltViewModel()
    val tourPending by guideViewModel.pending.collectAsStateWithLifecycle()
    // Cleared the moment it starts rather than when it ends: a tour that has
    // begun has been delivered, and clearing it at the end races the effect
    // that starts it — which showed up as the tour beginning again from step
    // one the instant it was finished.
    LaunchedEffect(tourPending) {
        if (tourPending && !guide.running) {
            guideViewModel.markSeen()
            guide.start()
        }
    }
    // Each step names the screen it belongs to and the app goes there, which
    // is what keeps the words and the thing being pointed at together.
    LaunchedEffect(guide.index) {
        val route = guide.step?.route ?: return@LaunchedEffect
        // Only when it is somewhere else. Re-navigating to the screen already
        // showing restores its saved state, which puts a long page like
        // Settings back to the top — and the tour walks down Settings a row
        // at a time, so every step after the first would lose its place.
        if (navController.currentDestination?.route == route) return@LaunchedEffect
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val nowPlaying: NowPlayingViewModel = hiltViewModel()
    val now by nowPlaying.player.state.collectAsStateWithLifecycle()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val selectedTab = tabs.firstOrNull { tab -> currentDestination?.hierarchy?.any { it.route == tab.route } == true }?.route

    CompositionLocalProvider(LocalGuide provides guide) {
    AppShell(
        playing = now?.takeIf { currentDestination?.route != Routes.LISTEN },
        selectedTab = selectedTab,
        onOpen = { playing ->
            val courseId = com.shahaabapps.twotracks.feature.listen.Episodes.courseOf(playing.episodeId)
            if (courseId != null) navController.navigate(Routes.listen(courseId, playing.episodeId)) { launchSingleTop = true }
        },
        onPlayPause = { playing -> if (playing.playing) nowPlaying.player.pause() else nowPlaying.player.play(playing.episodeId) },
        onForward30 = nowPlaying.player::forward,
        onTab = { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
    ) { padding ->
        val tabRoutes = tabs.map { it.route }.toSet()
        fun isTab(route: String?) = route in tabRoutes
        // One cap for the whole graph. The route is read here rather than
        // inside each destination so that nothing but this line knows about
        // it; the screens that want the width say so in [wideRoutes].
        ReadableColumn(wide = currentDestination?.route in wideRoutes) {
        NavHost(
            navController = navController,
            startDestination = Routes.TODAY,
            modifier = Modifier.padding(padding),
            // Settings used to fade here rather than slide, to hide the
            // cost of its first composition behind an animation with no
            // position to judge it by. That was concealment, and it did not
            // work: it still read as laggy. The cost was the debug build —
            // 164 classes verified on the main thread in one open, 221ms of
            // it, and a 520ms frame. Released, that frame is 90ms and nothing
            // is verified at all. Settings slides like every other screen.
            enterTransition = {
                if (isTab(initialState.destination.route) && isTab(targetState.destination.route)) fadeIn(tween(220))
                else slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(200))
            },
            exitTransition = {
                if (isTab(initialState.destination.route) && isTab(targetState.destination.route)) fadeOut(tween(220))
                else slideOutHorizontally(tween(320, easing = FastOutSlowInEasing)) { -it / 4 } + fadeOut(tween(300))
            },
            popEnterTransition = { slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { -it / 4 } + fadeIn(tween(200)) },
            popExitTransition = { slideOutHorizontally(tween(320, easing = FastOutSlowInEasing)) { it } + fadeOut(tween(200)) },
        ) {
            composable(Routes.TODAY) {
                TodayScreen(
                    onOpenLevel = { navController.navigate(Routes.level(it)) },
                    onOpenPartner = { navController.navigate(Routes.PARTNER) },
                    onOpenGoal = { navController.navigate(Routes.goalDetail(it)) },
                    onOpenWeek = { navController.navigate(Routes.WEEK) },
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                    onOpenChangelog = { navController.navigate(Routes.CHANGELOG) },
                    onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) },
                    onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) },
                )
            }
            composable(Routes.CHANGELOG) { ChangelogScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.WIDGETS) { WidgetsScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.US) {
                UsScreen(onNavigate = { navController.navigate(it) }, onOpenLevel = { navController.navigate(Routes.level(it)) })
            }
            composable(Routes.PARTNER) {
                PartnerScreen(
                    onBack = { navController.popBackStack() },
                    onOpenLevel = { navController.navigate(Routes.level(it)) },
                    onOpenGoal = { navController.navigate(Routes.goalDetail(it)) },
                )
            }
            composable(Routes.LEVELS) {
                LevelsScreen(onBack = { navController.popBackStack() }, onOpenLevel = { navController.navigate(Routes.level(it)) })
            }
            composable(Routes.LEARN) {
                LearnScreen(onOpenCourse = { navController.navigate(Routes.course(it)) }, onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) }, onOpenDeck = { navController.navigate(Routes.deck(it)) }, onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) })
            }
            composable(Routes.DECK, arguments = listOf(navArgument("courseId") { type = NavType.StringType })) {
                DeckScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.COURSE, arguments = listOf(navArgument("courseId") { type = NavType.StringType })) { entry ->
                CourseScreen(
                    courseId = entry.arguments?.getString("courseId").orEmpty(),
                    onBack = { navController.popBackStack() },
                    onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) },
                    onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) },
                    onOpenFiles = { c, m -> navController.navigate(Routes.moduleFiles(c, m)) },
                )
            }
            composable(Routes.MODULE_FILES, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("moduleId") { type = NavType.StringType })) {
                ModuleFilesScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.TOPIC, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("topicId") { type = NavType.StringType })) {
                TopicScreen(
                    onBack = { navController.popBackStack() },
                    onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) { popUpTo(Routes.COURSE) } },
                    onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) { popUpTo(Routes.COURSE) } },
                    onListen = { c, t -> navController.navigate(Routes.listen(c, t)) },
                    onOpenNotes = { c, t -> navController.navigate(Routes.notes(c, t)) },
                )
            }
            composable(
                Routes.LISTEN, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("topicId") { type = NavType.StringType }),
                // Up from below, as a now-playing screen comes, and down again.
                enterTransition = { slideInVertically(tween(360, easing = FastOutSlowInEasing)) { it } },
                exitTransition = { fadeOut(tween(200)) },
                popEnterTransition = { fadeIn(tween(200)) },
                popExitTransition = { slideOutVertically(tween(320, easing = FastOutSlowInEasing)) { it } },
            ) {
                ListenScreen(
                    onBack = { navController.popBackStack() },
                    onOpenNotes = { topicId ->
                        val courseId = it.arguments?.getString("courseId").orEmpty()
                        navController.navigate(Routes.notes(courseId, topicId))
                    },
                )
            }
            composable(Routes.NOTES, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("topicId") { type = NavType.StringType })) { entry ->
                NotesScreen(
                    courseId = entry.arguments?.getString("courseId").orEmpty(),
                    topicId = entry.arguments?.getString("topicId").orEmpty(),
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.QUIZ, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("moduleId") { type = NavType.StringType })) {
                QuizScreen(
                    onBack = { navController.popBackStack() },
                    onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) },
                )
            }
            composable(Routes.LEVEL, arguments = listOf(navArgument("levelId") { type = NavType.StringType })) { entry ->
                val lid = entry.arguments?.getString("levelId").orEmpty()
                LevelScreen(
                    levelId = lid,
                    onBack = { navController.popBackStack() },
                    onOpenGoal = { navController.navigate(Routes.goalDetail(it)) },
                    onNewGoal = { navController.navigate(Routes.newGoal(lid)) },
                )
            }
            composable(Routes.YOUR_WEEK) {
                YourWeekScreen(onBack = { navController.popBackStack() }, onNavigate = { navController.navigate(it) }, onOpenGoal = { navController.navigate(Routes.goalDetail(it)) })
            }
            composable(Routes.WEEK) {
                WeekScreen(
                    onBack = { navController.popBackStack() },
                    onOpenGoal = { navController.navigate(Routes.goalDetail(it)) },
                    onOpenBothWeeks = { navController.navigate(Routes.BOTH_WEEKS) },
                    onOpenFit = { navController.navigate(Routes.YOUR_WEEK) },
                )
            }
            composable(Routes.BOTH_WEEKS) { BothWeeksScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.GOAL_DETAIL, arguments = listOf(navArgument("goalId") { type = NavType.StringType })) { entry ->
                val goalId = entry.arguments?.getString("goalId").orEmpty()
                GoalDetailScreen(goalId = goalId, onBack = { navController.popBackStack() }, onEdit = { navController.navigate(Routes.goalEdit(goalId)) }, onOpenCourse = { navController.navigate(Routes.course(it)) }, onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) })
            }
            composable(Routes.GOAL_NEW, arguments = listOf(navArgument("levelId") { type = NavType.StringType; nullable = true; defaultValue = null })) {
                GoalEditScreen(goalId = null, onDone = { navController.popBackStack() })
            }
            composable(Routes.GOAL_EDIT, arguments = listOf(navArgument("goalId") { type = NavType.StringType })) { entry ->
                GoalEditScreen(goalId = entry.arguments?.getString("goalId"), onDone = { navController.popBackStack() })
            }
            composable(Routes.COUNTRIES) {
                CountriesScreen(onBack = { navController.popBackStack() }, onOpen = { navController.navigate(Routes.country(it)) })
            }
            composable(Routes.COUNTRY_DETAIL, arguments = listOf(navArgument("code") { type = NavType.StringType })) { entry ->
                CountryDetailScreen(code = entry.arguments?.getString("code").orEmpty(), onBack = { navController.popBackStack() })
            }
            composable(Routes.TESTS) { TestsScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.DOCUMENTS) { DocumentsScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.BUDGET) { BudgetScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onOpenAvailability = { navController.navigate(Routes.AVAILABILITY) },
                    onOpenVoices = { navController.navigate(Routes.VOICES) },
                    onOpenWidgets = { navController.navigate(Routes.WIDGETS) },
                    onOpenChangelog = { navController.navigate(Routes.CHANGELOG) },
                )
            }
            composable(Routes.AVAILABILITY) { AvailabilityScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.VOICES) { VoiceSettingsScreen(onBack = { navController.popBackStack() }) }
        }
        }
    }

    // Above the scaffold rather than inside it, so a step can ring the tab bar
    // as readily as anything on a screen.
    GuideOverlay(guide)
    }
}

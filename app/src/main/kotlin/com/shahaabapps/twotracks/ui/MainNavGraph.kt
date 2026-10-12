package com.shahaabapps.twotracks.ui

import com.shahaabapps.twotracks.feature.learn.DeckScreen
import com.shahaabapps.twotracks.feature.learn.CourseScreen
import com.shahaabapps.twotracks.feature.learn.LearnScreen
import com.shahaabapps.twotracks.feature.learn.QuizScreen
import com.shahaabapps.twotracks.feature.learn.TopicScreen
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.People
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
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
    const val DECK = "learn/{courseId}/deck"
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
    fun deck(courseId: String) = "learn/$courseId/deck"
    fun goalDetail(id: String) = "goal/$id"
    fun goalEdit(id: String) = "goal/$id/edit"
    fun newGoal(levelId: String? = null) = if (levelId == null) "goal/new/edit" else "goal/new/edit?levelId=$levelId"
    fun country(code: String) = "countries/$code"
}

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

/** Which tour anchor a tab reports itself as. */
private fun guideAnchorFor(route: String): String = when (route) {
    Routes.LEARN -> GuideAnchors.TAB_LEARN
    Routes.TODAY -> GuideAnchors.TAB_TODAY
    else -> GuideAnchors.TAB_US
}

/** The Parallel tab bar: the page colour under a hairline, ink for the tab you are on. */
@Composable
private fun CupertinoTabBar(selectedRoute: String?, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.groupedBackground)) {
        HorizontalDivider(color = AppTheme.colors.separator, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 6.dp).selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                val selected = tab.route == selectedRoute
                val tint = if (selected) AppTheme.colors.ink else AppTheme.colors.secondaryLabel
                val interaction = remember { MutableInteractionSource() }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .selectable(selected = selected, role = Role.Tab, interactionSource = interaction, indication = null) { onSelect(tab.route) }
                        .padding(vertical = 6.dp)
                        .guideAnchor(guideAnchorFor(tab.route)),
                ) {
                    Icon(tab.icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                    Text(
                        tab.label, style = MaterialTheme.typography.labelSmall, color = tint,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
        Column(modifier = Modifier.navigationBarsPadding()) {}
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

    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val selectedTab = tabs.firstOrNull { tab -> currentDestination?.hierarchy?.any { it.route == tab.route } == true }?.route

    CompositionLocalProvider(LocalGuide provides guide) {
    Scaffold(
        containerColor = AppTheme.colors.groupedBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (selectedTab != null) {
                CupertinoTabBar(selectedRoute = selectedTab) { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
    ) { padding ->
        val tabRoutes = tabs.map { it.route }.toSet()
        fun isTab(route: String?) = route in tabRoutes
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
                )
            }
            composable(Routes.TOPIC, arguments = listOf(navArgument("courseId") { type = NavType.StringType }, navArgument("topicId") { type = NavType.StringType })) {
                TopicScreen(
                    onBack = { navController.popBackStack() },
                    onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) { popUpTo(Routes.COURSE) } },
                    onOpenQuiz = { c, m -> navController.navigate(Routes.quiz(c, m)) { popUpTo(Routes.COURSE) } },
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

    // Above the scaffold rather than inside it, so a step can ring the tab bar
    // as readily as anything on a screen.
    GuideOverlay(guide)
    }
}

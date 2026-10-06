package com.shahaabapps.twotracks.ui

import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.shahaabapps.twotracks.feature.study.GoalDetailScreen
import com.shahaabapps.twotracks.feature.study.GoalEditScreen
import com.shahaabapps.twotracks.feature.tests.TestsScreen
import com.shahaabapps.twotracks.feature.today.TodayScreen
import com.shahaabapps.twotracks.feature.us.UsScreen
import com.shahaabapps.twotracks.feature.week.WeekScreen
import com.shahaabapps.twotracks.feature.week.YourWeekScreen
import com.shahaabapps.twotracks.session.AppSession

object Routes {
    const val TODAY = "today"
    const val US = "us"
    const val LEVELS = "levels"
    const val LEARN = "learn"
    const val COURSE = "learn/{courseId}"
    const val TOPIC = "learn/{courseId}/topic/{topicId}"
    const val QUIZ = "learn/{courseId}/quiz/{moduleId}"
    const val LEVEL = "level/{levelId}"
    const val YOUR_WEEK = "yourweek"
    const val PARTNER = "partner"
    const val CHANGELOG = "changelog"
    const val WEEK = "week"
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

    fun level(id: String) = "level/$id"
    fun course(id: String) = "learn/$id"
    fun topic(courseId: String, topicId: String) = "learn/$courseId/topic/$topicId"
    fun quiz(courseId: String, moduleId: String) = "learn/$courseId/quiz/$moduleId"
    fun goalDetail(id: String) = "goal/$id"
    fun goalEdit(id: String) = "goal/$id/edit"
    fun newGoal(levelId: String? = null) = if (levelId == null) "goal/new/edit" else "goal/new/edit?levelId=$levelId"
    fun country(code: String) = "countries/$code"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab(Routes.TODAY, "Today", Icons.Filled.Today),
    Tab(Routes.US, "US", Icons.Filled.Favorite),
    Tab(Routes.LEVELS, "Levels", Icons.Filled.Flag),
    Tab(Routes.LEARN, "Learn", Icons.Filled.MenuBook),
)

/** iOS tab bar: flat surface with a hairline on top, icon + 10sp label, accent when selected. */
@Composable
private fun CupertinoTabBar(selectedRoute: String?, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.card)) {
        HorizontalDivider(color = AppTheme.colors.separator, thickness = 0.5.dp)
        Row(
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEach { tab ->
                val selected = tab.route == selectedRoute
                val tint = if (selected) AppTheme.colors.accent else AppTheme.colors.secondaryLabel
                val interaction = remember { MutableInteractionSource() }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f).clickable(interactionSource = interaction, indication = null) { onSelect(tab.route) }.padding(vertical = 4.dp),
                ) {
                    Icon(tab.icon, contentDescription = tab.label, tint = tint, modifier = Modifier.size(24.dp))
                    Text(tab.label, style = MaterialTheme.typography.labelSmall, color = tint, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
        Column(modifier = Modifier.navigationBarsPadding()) {}
    }
}

@Composable
fun MainNavGraph(session: AppSession) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val selectedTab = tabs.firstOrNull { tab -> currentDestination?.hierarchy?.any { it.route == tab.route } == true }?.route

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
                    onOpenWeek = { navController.navigate(Routes.YOUR_WEEK) },
                    onOpenChangelog = { navController.navigate(Routes.CHANGELOG) },
                )
            }
            composable(Routes.CHANGELOG) { ChangelogScreen(onBack = { navController.popBackStack() }) }
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
                LevelsScreen(onOpenLevel = { navController.navigate(Routes.level(it)) }, onNavigate = { navController.navigate(it) })
            }
            composable(Routes.LEARN) {
                LearnScreen(onOpenCourse = { navController.navigate(Routes.course(it)) }, onOpenTopic = { c, t -> navController.navigate(Routes.topic(c, t)) })
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
                    onOpenLevel = { navController.navigate(Routes.level(it)) },
                    onNewGoal = { navController.navigate(Routes.newGoal(lid)) },
                )
            }
            composable(Routes.YOUR_WEEK) {
                YourWeekScreen(onBack = { navController.popBackStack() }, onNavigate = { navController.navigate(it) }, onOpenGoal = { navController.navigate(Routes.goalDetail(it)) })
            }
            composable(Routes.WEEK) { WeekScreen(onBack = { navController.popBackStack() }, onOpenGoal = { navController.navigate(Routes.goalDetail(it)) }) }
            composable(Routes.GOAL_DETAIL, arguments = listOf(navArgument("goalId") { type = NavType.StringType })) { entry ->
                val goalId = entry.arguments?.getString("goalId").orEmpty()
                GoalDetailScreen(goalId = goalId, onBack = { navController.popBackStack() }, onEdit = { navController.navigate(Routes.goalEdit(goalId)) })
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
                    onOpenChangelog = { navController.navigate(Routes.CHANGELOG) },
                )
            }
            composable(Routes.AVAILABILITY) { AvailabilityScreen(onBack = { navController.popBackStack() }) }
        }
    }
}

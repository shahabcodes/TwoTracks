package com.shahaabapps.twotracks.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.feature.budget.BudgetScreen
import com.shahaabapps.twotracks.feature.countries.CountriesScreen
import com.shahaabapps.twotracks.feature.countries.CountryDetailScreen
import com.shahaabapps.twotracks.feature.documents.DocumentsScreen
import com.shahaabapps.twotracks.feature.more.MoreScreen
import com.shahaabapps.twotracks.feature.progress.ProgressScreen
import com.shahaabapps.twotracks.feature.roadmap.RoadmapScreen
import com.shahaabapps.twotracks.feature.settings.AvailabilityScreen
import com.shahaabapps.twotracks.feature.settings.SettingsScreen
import com.shahaabapps.twotracks.feature.study.GoalDetailScreen
import com.shahaabapps.twotracks.feature.study.GoalEditScreen
import com.shahaabapps.twotracks.feature.study.StudyScreen
import com.shahaabapps.twotracks.feature.tests.TestsScreen
import com.shahaabapps.twotracks.feature.today.TodayScreen
import com.shahaabapps.twotracks.feature.week.WeekScreen
import com.shahaabapps.twotracks.session.AppSession

object Routes {
    const val TODAY = "today"
    const val WEEK = "week"
    const val STUDY = "study"
    const val PROGRESS = "progress"
    const val MORE = "more"
    const val GOAL_DETAIL = "goal/{goalId}"
    const val GOAL_EDIT = "goal/{goalId}/edit"
    const val GOAL_NEW = "goal/new/edit"
    const val ROADMAP = "roadmap"
    const val COUNTRIES = "countries"
    const val COUNTRY_DETAIL = "countries/{code}"
    const val TESTS = "tests"
    const val DOCUMENTS = "documents"
    const val BUDGET = "budget"
    const val SETTINGS = "settings"
    const val AVAILABILITY = "settings/availability"

    fun goalDetail(id: String) = "goal/$id"
    fun goalEdit(id: String) = "goal/$id/edit"
    fun country(code: String) = "countries/$code"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab(Routes.TODAY, "Today", Icons.Filled.Today),
    Tab(Routes.WEEK, "Week", Icons.Filled.CalendarMonth),
    Tab(Routes.STUDY, "Study", Icons.Filled.MenuBook),
    Tab(Routes.PROGRESS, "Progress", Icons.Filled.Insights),
    Tab(Routes.MORE, "More", Icons.Filled.MoreHoriz),
)

@Composable
fun MainNavGraph(session: AppSession) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination
    val showBar = tabs.any { tab -> currentDestination?.hierarchy?.any { it.route == tab.route } == true }

    Scaffold(
        containerColor = AppTheme.colors.groupedBackground,
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = AppTheme.colors.card) {
                    tabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AppTheme.colors.accent,
                                selectedTextColor = AppTheme.colors.accent,
                                indicatorColor = AppTheme.colors.accent.copy(alpha = 0.12f),
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.TODAY,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.TODAY) {
                TodayScreen(onOpenGoal = { navController.navigate(Routes.goalDetail(it)) })
            }
            composable(Routes.WEEK) {
                WeekScreen(onOpenGoal = { navController.navigate(Routes.goalDetail(it)) })
            }
            composable(Routes.STUDY) {
                StudyScreen(
                    onOpenGoal = { navController.navigate(Routes.goalDetail(it)) },
                    onNewGoal = { navController.navigate(Routes.GOAL_NEW) },
                )
            }
            composable(Routes.PROGRESS) { ProgressScreen() }
            composable(Routes.MORE) {
                MoreScreen(onNavigate = { navController.navigate(it) })
            }
            composable(Routes.GOAL_DETAIL, arguments = listOf(navArgument("goalId") { type = NavType.StringType })) { entry ->
                val goalId = entry.arguments?.getString("goalId").orEmpty()
                GoalDetailScreen(
                    goalId = goalId,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(Routes.goalEdit(goalId)) },
                )
            }
            composable(Routes.GOAL_NEW) {
                GoalEditScreen(goalId = null, onDone = { navController.popBackStack() })
            }
            composable(Routes.GOAL_EDIT, arguments = listOf(navArgument("goalId") { type = NavType.StringType })) { entry ->
                GoalEditScreen(goalId = entry.arguments?.getString("goalId"), onDone = { navController.popBackStack() })
            }
            composable(Routes.ROADMAP) { RoadmapScreen(onBack = { navController.popBackStack() }) }
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
                SettingsScreen(onBack = { navController.popBackStack() }, onOpenAvailability = { navController.navigate(Routes.AVAILABILITY) })
            }
            composable(Routes.AVAILABILITY) { AvailabilityScreen(onBack = { navController.popBackStack() }) }
        }
    }
}

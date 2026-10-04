package com.shahaabapps.twotracks.ui

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
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

/** iOS tab bar (Browse Safe bg_tab_bar): flat surface with a hairline on top, icon + 10sp label, accent when selected. */
@Composable
private fun CupertinoTabBar(selectedRoute: String?, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.card)) {
        HorizontalDivider(color = AppTheme.colors.separator, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 6.dp),
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
                    modifier = Modifier
                        .weight(1f)
                        .clickable(interactionSource = interaction, indication = null) { onSelect(tab.route) }
                        .padding(vertical = 4.dp),
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
        // Let each screen handle the status bar itself so the hero can run edge to edge.
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

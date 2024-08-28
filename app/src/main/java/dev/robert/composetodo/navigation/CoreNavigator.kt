package dev.robert.composetodo.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.robert.composetodo.utils.ConstantUtils
import dev.robert.navigation.auth.AuthNavGraph
import dev.robert.navigation.auth.RegisterScreen
import dev.robert.navigation.auth.authNavGraph
import dev.robert.navigation.onboarding.onBoardingNavGraph
import dev.robert.navigation.profile.ProfileNavGraph
import dev.robert.navigation.profile.profileNavGraph
import dev.robert.navigation.settings.ChangePasswordScreen
import dev.robert.navigation.settings.settingsNavGraph
import dev.robert.navigation.tasks.Task
import dev.robert.navigation.tasks.TasksNavGraph
import dev.robert.navigation.tasks.TasksScreen
import dev.robert.navigation.tasks.tasksNavGraph

@Composable
fun CoreNavigator(
    startDestination: Any,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onBoardingNavGraph(
            onCompleteOnBoarding = {
                navController.navigate(
                    AuthNavGraph,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(navController.graph.id, true)
                        .build(),
                )
            }
        )
        authNavGraph(
            onNavigateToHome = {
                navController.navigate(
                    TasksNavGraph,
                    navOptions =
                    NavOptions.Builder()
                        .setPopUpTo(navController.graph.id, true)
                        .build(),
                )
            },
            onNavigateToRegister = {
                navController.navigate(RegisterScreen)
            },
            onNavigateUp = {
                navController.navigateUp()
            }
        )
        tasksNavGraph(
            onNavigateToDetails = { taskItem ->
                navController.navigate(Task(item = taskItem)) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(TasksScreen) {
                        inclusive = false
                    }
                }
            },
            onNavigateUp = {
                navController.navigateUp()
            },
        )
        profileNavGraph()

        settingsNavGraph(
            onNavigateToProfile = {
                navController.navigate(ProfileNavGraph)
            },
            onNavigateToChangePassword = {
                navController.navigate(ChangePasswordScreen)
            },
            onNavigateToRateApp = {
                val uri = Uri.parse(ConstantUtils.RATE_APP_URL)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            onNavigateToReportIssue = {
                val uri = Uri.parse(ConstantUtils.REPORT_ISSUE_URL)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            onNavigateToAbout = {
                val uri = Uri.parse(ConstantUtils.RATE_APP_URL)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            onNavigateToLogin = {
                navController.navigate(
                    route = AuthNavGraph,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(navController.graph.id, true)
                        .build(),
                )
            }
        )
    }
}

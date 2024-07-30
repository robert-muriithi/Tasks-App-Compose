package dev.robert.composetodo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.auth.presentation.navigation.RegisterScreen
import dev.robert.auth.presentation.navigation.authNavGraph
import dev.robert.design_system.presentation.components.TDAppBar
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.utils.scaleIntoContainer
import dev.robert.design_system.presentation.utils.scaleOutOfContainer
import dev.robert.onboarding.presentation.navigation.onBoardingNavGraph
import dev.robert.tasks.presentation.navigation.AddTaskScreen
import dev.robert.tasks.presentation.navigation.TasksNavGraph
import dev.robert.tasks.presentation.navigation.TasksScreen
import dev.robert.tasks.presentation.navigation.TodoItem
import dev.robert.tasks.presentation.navigation.tasksNavGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TodoCoreNavigator(
    startDestination: Any,
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
) {
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
            onNavigateToDetails = { name, age ->
                navController.navigate(route = TodoItem(name = name, age = age))
            },
            onNavigateToAddTask = {
                navController.navigate(AddTaskScreen)
            },
            onNavigateUp = {
                navController.navigateUp()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    startDestination: Any,
    navController: NavHostController,
    scope: CoroutineScope
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showDrawer = listOf(
        TasksScreen::class,
        AddTaskScreen::class,
        TodoItem::class
    ).any { currentDestination?.hasRoute(it) == true }

    TDSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                if (showDrawer) {
                }
            },
            drawerState = drawerState,
            gesturesEnabled = showDrawer
        ) {
            Scaffold(
                topBar = {
                    if (showDrawer) TDAppBar(
                        title = {
                            when (currentDestination?.route) {
                                TasksScreen::class.qualifiedName -> Text(text = "Welcome User")
                                AddTaskScreen::class.qualifiedName -> Text(text = "Add Task")
                                TodoItem::class.qualifiedName -> Text(text = "Task Details")
                                else -> Text(text = "Welcome User")
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (currentDestination?.hasRoute(TasksScreen::class) == true) scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                    else navController.navigateUp()
                                }) {
                                Icon(
                                    imageVector = if (currentDestination?.hasRoute(TasksScreen::class) == true) Icons.Default.Menu else Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = currentDestination?.hasRoute(TasksScreen::class) == true,
                        enter = scaleIntoContainer(direction = AnimatedContentTransitionScope.SlideDirection.Right),
                        exit = scaleOutOfContainer(direction = AnimatedContentTransitionScope.SlideDirection.Left)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(AddTaskScreen)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            ) { paddingValues ->
                TodoCoreNavigator(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

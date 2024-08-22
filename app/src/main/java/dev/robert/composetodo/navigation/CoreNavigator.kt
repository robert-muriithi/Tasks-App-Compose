package dev.robert.composetodo.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.auth.presentation.navigation.RegisterScreen
import dev.robert.auth.presentation.navigation.authNavGraph
import dev.robert.compose_todo.R
import dev.robert.design_system.presentation.components.NavDrawerItem
import dev.robert.design_system.presentation.components.NavigationDrawerContent
import dev.robert.design_system.presentation.components.TDAppBar
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.components.UserObject
import dev.robert.onboarding.presentation.navigation.onBoardingNavGraph
import dev.robert.profile.presentation.navigation.ProfileNavGraph
import dev.robert.profile.presentation.navigation.ProfileScreen
import dev.robert.profile.presentation.navigation.profileNavGraph
import dev.robert.tasks.presentation.navigation.AddTaskScreen
import dev.robert.tasks.presentation.navigation.Task
import dev.robert.tasks.presentation.navigation.TasksNavGraph
import dev.robert.tasks.presentation.navigation.TasksScreen
import dev.robert.tasks.presentation.navigation.tasksNavGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TodoCoreNavigator(
    startDestination: Any,
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    onUpdateGridState: (Boolean) -> Unit
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
            onNavigateToDetails = { taskItem ->
                navController.navigate(Task(item = taskItem))
            },
            onNavigateUp = {
                navController.navigateUp()
            },
        )
        profileNavGraph { navController.navigateUp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    startDestination: Any,
    navController: NavHostController,
    scope: CoroutineScope,
    onSignOut: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showDrawer = listOf(
        TasksScreen::class,
    ).any { currentDestination?.hasRoute(it) == true }
    val user = FirebaseAuth.getInstance().currentUser
    val userObject = UserObject(
        displayName = user?.displayName,
        email = user?.email ?: "",
        photoUrl = user?.photoUrl?.toString()
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val onUpdateGridState: (Boolean) -> Unit = {
    }
    TDSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                if (showDrawer) {
                    ModalDrawerSheet {
                        NavigationDrawerContent(
                            user = userObject,
                            modifier = Modifier.fillMaxWidth(),
                            selectedItem = selectedIndex,
                            onTap = { title, index ->
                                selectedIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                                when (title) {
                                    NavDrawerItem.Home.title -> navController.navigate(
                                        TasksScreen
                                    )
                                    NavDrawerItem.Profile.title -> navController.navigate(
                                        ProfileNavGraph
                                    )
                                    NavDrawerItem.Settings.title -> navController.navigate(
                                        AuthNavGraph
                                    )
                                    NavDrawerItem.Logout.title -> {
                                        onSignOut()
                                        navController.navigate(
                                            route = AuthNavGraph,
                                            navOptions = NavOptions.Builder()
                                                .setPopUpTo(navController.graph.id, true)
                                                .build(),
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            },
            drawerState = drawerState,
            gesturesEnabled = showDrawer
        ) {
            val showAppBar = listOf(
                TasksScreen::class,
                Task::class,
                ProfileScreen::class
            ).any { currentDestination?.hasRoute(it) == true }

            Scaffold(
                topBar = {
                    if (showAppBar) TDAppBar(
                        title = {
                            when (currentDestination?.route) {
                                TasksScreen::class.qualifiedName -> Text(text = "Welcome, ${user?.displayName?.split(" ")?.first()}")
                                AddTaskScreen::class.qualifiedName -> Text(text = "Add Task")
                                Task::class.qualifiedName -> Text(text = "Task Details")
                                ProfileScreen::class.qualifiedName -> Text(text = "Profile")
                                else -> {}
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
                                    painter = painterResource(id = if (currentDestination?.hasRoute(TasksScreen::class) == true) R.drawable.bars_staggered_solid else R.drawable.arrow_left_solid),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        actions = {
                            if (currentDestination?.hasRoute(TasksScreen::class) == true) {
                                IconButton(onClick = { }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                                }
                            }

                            if (currentDestination?.hasRoute(Task::class) == true) {
                                IconButton(
                                    onClick = {
                                        // navController.navigate(AddTaskScreen)
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = currentDestination?.hasRoute(TasksScreen::class) == true,
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
                    modifier = Modifier.padding(paddingValues),
                    onUpdateGridState = onUpdateGridState
                )
            }
        }
    }
}

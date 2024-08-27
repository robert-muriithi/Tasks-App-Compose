package dev.robert.composetodo.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.robert.auth.presentation.navigation.AuthNavGraph
import dev.robert.auth.presentation.navigation.RegisterScreen
import dev.robert.auth.presentation.navigation.authNavGraph
import dev.robert.compose_todo.R
import dev.robert.composetodo.utils.ConstantUtils
import dev.robert.design_system.presentation.components.NavDrawerItem
import dev.robert.design_system.presentation.components.NavigationDrawerContent
import dev.robert.design_system.presentation.components.TDAppBar
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.components.UserObject
import dev.robert.onboarding.presentation.navigation.onBoardingNavGraph
import dev.robert.profile.presentation.navigation.ProfileNavGraph
import dev.robert.profile.presentation.navigation.ProfileScreen
import dev.robert.profile.presentation.navigation.profileNavGraph
import dev.robert.settings.presentation.navigation.ChangePasswordScreen
import dev.robert.settings.presentation.navigation.SettingsNavGraph
import dev.robert.settings.presentation.navigation.SettingsScreen
import dev.robert.settings.presentation.navigation.settingsNavGraph
import dev.robert.tasks.presentation.navigation.AddTaskScreen
import dev.robert.tasks.presentation.navigation.SearchScreen
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
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    startDestination: Any,
    navController: NavHostController,
    scope: CoroutineScope,
    onSignOut: () -> Unit,
    userObject: UserObject
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showDrawer = listOf(
        TasksScreen::class,
    ).any { currentDestination?.hasRoute(it) == true }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    // TODO: FIX THIS/ OR FIND BETTER APPROACH.. it's always reseting to 0 when user navigates back
    LaunchedEffect(currentDestination) {
        selectedIndex = when {
            currentDestination?.hasRoute(TasksScreen::class) == true -> 0
            currentDestination?.hasRoute(ProfileScreen::class) == true -> 1
            currentDestination?.hasRoute(SettingsScreen::class) == true -> 2
            else -> 0
        }
    }

    TDSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                if (showDrawer) {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.background,
                    ) {
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
                                        SettingsNavGraph
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
                ProfileScreen::class,
                SearchScreen::class,
                SettingsScreen::class,
                ChangePasswordScreen::class
            ).any { currentDestination?.hasRoute(it) == true }

            Scaffold(
                topBar = {
                    if (showAppBar) TDAppBar(
                        title = {
                            when {
                                currentDestination?.hasRoute(Task::class) == true -> Text(text = "${navBackStackEntry?.toRoute<Task>()?.item?.name}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                                currentDestination?.hasRoute(TasksScreen::class) == true -> Text(text = "Welcome, ${userObject.displayName?.split(" ")?.first()}")
                                currentDestination?.hasRoute(SettingsScreen::class) == true -> Text(text = "Settings")
                                currentDestination?.hasRoute(ProfileScreen::class) == true -> Text(text = "Profile")
                                currentDestination?.hasRoute(ChangePasswordScreen::class) == true -> Text(text = "Change Password")
                                currentDestination?.hasRoute(SearchScreen::class) == true -> TDFilledTextField(
                                    onValueChange = {},
                                    value = "",
                                    label = "Search",
                                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                        .height(48.dp),
                                    trailingIcon = {
                                        // show clear search when text is not empty
                                        IconButton(
                                            onClick = {
                                                // clear search
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                                        }
                                    }
                                )
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
                                IconButton(onClick = {
                                    navController.navigate(SearchScreen)
                                }) {
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
                        },
                    )
                },
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = currentDestination?.hasRoute(TasksScreen::class) == true,
                    ) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(AddTaskScreen)
                            },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
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
                )
            }
        }
    }
}

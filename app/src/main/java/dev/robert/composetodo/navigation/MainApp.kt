/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.composetodo.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.robert.auth.domain.model.GoogleUser
import dev.robert.compose_todo.R
import dev.robert.design_system.presentation.components.NavDrawerItem
import dev.robert.design_system.presentation.components.NavigationDrawerContent
import dev.robert.design_system.presentation.components.TDAppBar
import dev.robert.design_system.presentation.components.TDSurface
import dev.robert.design_system.presentation.components.UserObject
import dev.robert.navigation.auth.AuthNavGraph
import dev.robert.navigation.profile.ProfileNavGraph
import dev.robert.navigation.profile.ProfileScreen
import dev.robert.navigation.settings.ChangePasswordScreen
import dev.robert.navigation.settings.SettingsNavGraph
import dev.robert.navigation.settings.SettingsScreen
import dev.robert.navigation.tasks.AddTaskScreen
import dev.robert.navigation.tasks.SearchScreen
import dev.robert.navigation.tasks.TasksScreen
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    startDestination: Any,
    navController: NavHostController,
    onSignOut: () -> Unit,
    onSaveUser: (GoogleUser) -> Unit,
    toggled: Boolean,
    userObject: UserObject
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showDrawer = listOf(
        TasksScreen::class,
    ).any { currentDestination?.hasRoute(it) == true }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    Timber.d("User Object: $userObject")

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
                ProfileScreen::class,
                SettingsScreen::class,
                ChangePasswordScreen::class
            ).any { currentDestination?.hasRoute(it) == true }

            Scaffold(
                topBar = {
                    if (showAppBar) TDAppBar(
                        title = {
                            when {
                                currentDestination?.hasRoute(TasksScreen::class) == true -> Text(
                                    text = "Welcome, ${userObject.displayName?.split(" ")?.first()}"
                                )

                                currentDestination?.hasRoute(SettingsScreen::class) == true -> Text(
                                    text = "Settings"
                                )

                                currentDestination?.hasRoute(ProfileScreen::class) == true -> Text(
                                    text = "Profile"
                                )

                                currentDestination?.hasRoute(ChangePasswordScreen::class) == true -> Text(
                                    text = "Change Password"
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (currentDestination?.hasRoute(TasksScreen::class) ==
                                        true) scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                    else navController.navigateUp()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (currentDestination?.hasRoute(
                                                TasksScreen::class
                                            ) == true
                                        ) R.drawable.bars_staggered_solid else R.drawable.arrow_left_solid
                                    ),
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
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
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
                CoreNavigator(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier
                        .padding(paddingValues),
                    onSaveUser = onSaveUser
                )
            }
        }
    }
}

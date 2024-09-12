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
package dev.robert.composetodo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.auth.R
import dev.robert.auth.presentation.utils.GoogleAuthSignInClient
import dev.robert.composetodo.navigation.MainApp
import dev.robert.design_system.presentation.components.UserObject
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.design_system.presentation.theme.TodoTheme
import dev.robert.design_system.presentation.theme.darkScrim
import dev.robert.design_system.presentation.theme.lightScrim
import dev.robert.resources.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.showSplashScreen.value
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            val theme by viewModel.currentTheme.collectAsState(
                initial = Theme.FOLLOW_SYSTEM.themeValue,
                context = Dispatchers.Main.immediate,
            )

            val user by viewModel.userData.collectAsState()
            val toggled by viewModel.isToggled.collectAsState()
            val startDestination by viewModel.startDestination.collectAsState()
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            val googleAuthUiClient by lazy {
                GoogleAuthSignInClient(
                    webClientId = applicationContext.getString(R.string.default_web_client_id),
                    oneTapClient = Identity.getSignInClient(applicationContext)
                )
            }
            val userObject = UserObject(
                displayName = user.name,
                email = user.email,
                photoUrl = user.photoUrl,
                id = user.id
            )
            TodoTheme(
                theme = theme,
            ) {
                MainApp(
                    startDestination = startDestination,
                    navController = navController,
                    onSignOut = {
                        scope.launch {
                            googleAuthUiClient.run {
                                signOut()
                                revokeAccess()
                            }
                        }
                        viewModel::clearUserData
                    },
                    userObject = userObject,
                    toggled = toggled,
                    onSaveUser = viewModel::setUser
                )
            }
        }
    }
}

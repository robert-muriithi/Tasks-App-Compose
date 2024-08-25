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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.auth.R
import dev.robert.auth.presentation.utils.GoogleAuthSignInClient
import dev.robert.composetodo.navigation.MainApp
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.design_system.presentation.theme.TodoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.showSplashScreen.value
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
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
            val startDestination by viewModel.startDestination.collectAsState()
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            val googleAuthUiClient by lazy {
                GoogleAuthSignInClient(
                    webClientId = applicationContext.getString(R.string.default_web_client_id),
                    oneTapClient = Identity.getSignInClient(applicationContext)
                )
            }
            TodoTheme(
                theme = theme,
            ) {
                MainApp(
                    startDestination = startDestination,
                    navController = navController,
                    scope = scope,
                    onSignOut = {
                        scope.launch {
                            googleAuthUiClient.run {
                                signOut()
                                revokeAccess()
                            }
                            viewModel.signOut()
                        }
                    }
                )
            }
        }
    }
}

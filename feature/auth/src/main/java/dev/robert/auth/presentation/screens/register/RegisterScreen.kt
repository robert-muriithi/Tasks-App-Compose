package dev.robert.auth.presentation.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegisterScreen(
    onNavigateUp: () -> Unit,
    onNavigate: () -> Unit,
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.action.collect {
            when (it) {
                RegisterAction.NavigateToLogin -> {
                    Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show()
                    onNavigateUp()
                }
            }
        }
    }
    Scaffold {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
        }
    }
}

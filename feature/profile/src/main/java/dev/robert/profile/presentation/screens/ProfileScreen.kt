package dev.robert.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.design_system.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.profile.collectAsStateWithLifecycle()
    val hasError = state.error.isNullOrBlank().not()

    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(ProfileScreenEvents.LoadProfile)
    }

    LaunchedEffect(key1 = hasError) {
        viewModel.action.collectLatest { action ->
            when (action) {
                is ProfileScreenActions.ShowError -> showDialog.value = true
                else -> {}
            }
        }
    }
    ErrorDialog(showDialog = showDialog, message = state.error ?: "")
    Box(modifier = Modifier.fillMaxSize()) {
        ProfileScreenContent(
            state = state,
            onNavigateBack = onNavigateBack,
            onEvent = viewModel::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProfileScreenContent(
    state: ProfileScreenState,
    onNavigateBack: () -> Unit,
    onEvent: (ProfileScreenEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfilePhoto(state = state)
        Text(
            text = state.profile?.name ?: "No Name",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = state.profile?.email ?: "No Email",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = { onEvent(ProfileScreenEvents.EditProfile) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Edit Profile")
        }
    }
}

@Composable
fun ProfilePhoto(
    state: ProfileScreenState
) {
    if (state.loginType == TodoAppPreferences.LOGIN_TYPE_GOOGLE) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.profile?.photoUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.account_circle_24px),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp),
        )
    } else {
        val name = state.profile?.name ?: ""
        val initial = name.firstOrNull()?.uppercaseChar() ?: 'A'
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape)
                .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial.toString(),
                style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp)
            )
        }
    }
}

@Composable
private fun ErrorDialog(
    showDialog: MutableState<Boolean>,
    message: String
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Error") },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

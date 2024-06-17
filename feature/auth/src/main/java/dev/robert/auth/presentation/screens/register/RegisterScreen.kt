package dev.robert.auth.presentation.screens.register

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.auth.R
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDOutlinedTextField
import dev.robert.design_system.presentation.components.TDSpacer

@Composable
fun RegisterScreen(
    onNavigateUp: () -> Unit,
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
    val scrollState = rememberScrollState()
    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            RegisterScreenContent(
                scrollState = scrollState,
                uiState = uiState,
                onEmailChange = viewModel::onEmailChanged,
                onPasswordChange = viewModel::onPasswordChanged,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
                onNameChanged = viewModel::onNameChanged,
                onSubmit = viewModel::register
            )
        }
    }
}
@Composable
fun RegisterScreenContent(
    scrollState: ScrollState,
    uiState: RegisterState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        TDOutlinedTextField(
            onValueChange = onNameChanged,
            value = uiState.name,
            isPassword = false,
            label = "Name",
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            isError = uiState.nameError != null,
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TDOutlinedTextField(
            isPassword = false,
            value = uiState.email,
            label = "Email",
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.mail_24px),
                    contentDescription = "Email Icon"
                )
            },
            onValueChange = onEmailChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = uiState.emailError != null,
        )
        if (uiState.emailError != null) Text(text = uiState.emailError)
        TDSpacer(modifier = Modifier.height(10.dp))
        TDOutlinedTextField(
            isPassword = true,
            value = uiState.password,
            label = "Password",
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = uiState.passwordError != null,
        )
        if (uiState.passwordError != null) Text(text = uiState.passwordError)
        TDOutlinedTextField(
            isPassword = true,
            value = uiState.confirmPassword,
            label = "Confirm Password",
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = uiState.confirmPasswordError != null,
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TDButton(onClick = onSubmit, text = "Register", enabled = uiState.buttonEnabled)
    }
}

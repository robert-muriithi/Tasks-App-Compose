package dev.robert.auth.presentation.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.auth.R
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateUp: () -> Unit,
) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.isSuccess) {
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
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painter = painterResource(id = R.drawable.bg),
            contentScale = ContentScale.FillBounds,
            sizeToIntrinsics = true,
            alpha = 0.2f
        )
    ) {
        RegisterScreenContent(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChanged,
            onPasswordChange = viewModel::onPasswordChanged,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
            onNameChanged = viewModel::onNameChanged,
            onSubmit = viewModel::register,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            onNavigateToLogin = onNavigateUp
        )
    }
}
@Composable
fun RegisterScreenContent(
    modifier: Modifier,
    uiState: RegisterState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TDFilledTextField(
            value = uiState.name,
            onValueChange = onNameChanged,
            label = "Name",
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.person_24dp),
                    contentDescription = "Person Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
        )
        if (uiState.nameError != null)
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(text = uiState.nameError, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
            } else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mail_24px),
                    contentDescription = "Email Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
        )
        if (uiState.emailError != null)
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(text = uiState.emailError, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
            }
        else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.lock_24dp),
                    contentDescription = "Password Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isPassword = true
        )
        if (uiState.passwordError != null) {
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.passwordError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm password",
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.lock_24dp),
                    contentDescription = "Password Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isPassword = true
        )
        if (uiState.confirmPasswordError != null) {
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.confirmPasswordError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDButton(
            onClick = onSubmit,
            text = "Register",
            enabled = true,
            modifier = Modifier
                .fillMaxWidth(0.9f),
            isLoading = uiState.isLoading
        )
        TDSpacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Already having an account?",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        TDSpacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

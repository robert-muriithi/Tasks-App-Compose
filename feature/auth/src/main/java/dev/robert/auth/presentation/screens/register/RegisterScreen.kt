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
import androidx.compose.ui.res.stringResource
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

@Composable
fun RegisterScreen(
    onNavigateUp: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.isSuccess) {
        viewModel.action.collect {
            when (it) {
                is RegisterAction.NavigateToLogin -> {
                    Toast.makeText(context, "Registration success", Toast.LENGTH_SHORT).show()
                    onNavigateUp()
                }

                is RegisterAction.ShowError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
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
            onNameChange = viewModel::onNameChanged,
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit,
    uiState: RegisterState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TDFilledTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.person_24dp),
                    contentDescription = stringResource(R.string.person_icon)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isLoading = uiState.isLoading
        )
        if (uiState.nameError != null && uiState.isLoading.not())
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.nameError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.mail_24px),
                    contentDescription = stringResource(R.string.email_icon)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isLoading = uiState.isLoading
        )
        if (uiState.emailError != null && uiState.isLoading.not())
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.emailError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        else
            TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.password),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.lock_24dp),
                    contentDescription = stringResource(R.string.password_icon)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isPassword = true,
            isLoading = uiState.isLoading
        )
        if (uiState.passwordError != null && uiState.isLoading.not()) {
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
            label = stringResource(R.string.confirm_password),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.lock_24dp),
                    contentDescription = stringResource(R.string.password_icon)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp),
            isPassword = true,
            isLoading = uiState.isLoading
        )
        if (uiState.confirmPasswordError != null && uiState.isLoading.not()) {
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
            text = stringResource(R.string.register),
            enabled = uiState.isLoading.not(),
            modifier = Modifier
                .fillMaxWidth(0.9f),
            isLoading = uiState.isLoading
        )
        TDSpacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.already_having_an_account),
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

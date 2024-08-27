package dev.robert.settings.presentation.screens.change_password

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.components.TDFilledTextField
import dev.robert.design_system.presentation.components.TDSpacer

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChangePasswordScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onInputChange = viewModel::onInputChange
    )
}

@Composable
private fun ChangePasswordScreenContent(
    uiState: ChangePasswordState,
    onEvent: (event: ChangePasswordEvents) -> Unit,
    onInputChange: (input: InputChange) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        TDFilledTextField(
            value = uiState.currentPassword,
            onValueChange = { onInputChange(InputChange.CurrentPasswordChange(it)) },
            label = "Current Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            isPassword = true,
            isLoading = uiState.isLoading
        )
        if (uiState.currentPasswordError != null && uiState.isLoading.not())
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.currentPassword,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        else TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.newPassword,
            onValueChange = { onInputChange(InputChange.NewPasswordChange(it)) },
            label = "New Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            isPassword = true,
            isLoading = uiState.isLoading
        )
        if (uiState.newPasswordError != null && uiState.isLoading.not())
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.newPasswordError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        else TDSpacer(modifier = Modifier.height(10.dp))
        TDFilledTextField(
            value = uiState.confirmPassword,
            onValueChange = { onInputChange(InputChange.ConfirmPasswordChange(it)) },
            label = "Confirm Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            isPassword = true,
            isLoading = uiState.isLoading
        )
        if (uiState.confirmPasswordError != null && uiState.isLoading.not())
            Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                Text(
                    text = uiState.confirmPasswordError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        else TDSpacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(
            visible = uiState.newPassword.isNotEmpty() || uiState.confirmPassword.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            WhatIsRequiredSection(
                conditions = uiState.strongPasswordsConditions
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TDButton(
            text = "Change Password",
            onClick = { onEvent(ChangePasswordEvents.ChangePassword) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState.isLoading,
            enabled = uiState.isLoading.not()
        )
    }
}

@Composable
private fun WhatIsRequiredSection(
    conditions: List<StrongPasswordCondition>
) {
    LazyColumn {
        item {
            Text(
                text = "What is required:",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        items(conditions.size) { index ->
            val condition = conditions[index]
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = if (condition.isMet) "✅" else "❌",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = condition.description,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (condition.isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreenContent(
        uiState = ChangePasswordState(
            strongPasswordsConditions = listOf(
                StrongPasswordCondition("At least 8 characters", true),
                StrongPasswordCondition("At least 1 uppercase letter", false),
                StrongPasswordCondition("At least 1 lowercase letter", true),
                StrongPasswordCondition("At least 1 number", true),
                StrongPasswordCondition("At least 1 special character", true),
            )
        ),
        onEvent = {},
        onInputChange = {}
    )
}

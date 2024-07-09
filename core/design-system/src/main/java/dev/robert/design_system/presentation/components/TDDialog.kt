package dev.robert.design_system.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TDResetPasswordDialog(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    value: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.Center) {
                TDResetPasswordDialogContent(
                    modifier = modifier,
                    onSubmit = onSubmit,
                    onDismiss = onDismiss,
                    onValueChange = onValueChange,
                    icon = icon,
                    value = value
                )
            }
        }
    }
}

@Composable
fun TDResetPasswordDialogContent(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    value: String
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.displayMedium
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .clickable { onDismiss() }
            )
        }
        TDSpacer(modifier = Modifier.height(20.dp))
        TDFilledTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp)
        )
        TDSpacer(modifier = Modifier.height(20.dp))
        TDButton(
            text = "Reset Password",
            onClick = { onSubmit(value) },
            modifier = Modifier.fillMaxWidth(0.9f),
            isLoading = false
        )
    }
}

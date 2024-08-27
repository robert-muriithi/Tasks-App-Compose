package dev.robert.design_system.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.robert.design_system.R

@Composable
fun TDResetPasswordDialog(
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
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
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.reset_password),
                style = MaterialTheme.typography.displayMedium
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
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
                    contentDescription = stringResource(R.string.email_icon)
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(53.dp)
        )
        TDSpacer(modifier = Modifier.height(20.dp))
        TDButton(
            text = stringResource(R.string.reset_password),
            onClick = { onSubmit(value) },
            modifier = Modifier.fillMaxWidth(0.9f),
            isLoading = false
        )
    }
}

@Composable
fun AlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    showCancel: Boolean = true,
    type: DialogType = DialogType.SUCCESS
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(top = 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 16.dp, end = 10.dp, start = 10.dp, bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (showCancel) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onError)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
                        ) {
                            Text("Ok", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    } else {
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
                        ) {
                            Text("Ok", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = (-50).dp)
                    .clip(CircleShape)
                    .background(
                        if (type == DialogType.SUCCESS) Color(0xFF66BB6A) else Color(
                            0xFFEF5350
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (type == DialogType.SUCCESS) "✔" else "✖",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun OptionsDialog(
    title: String,
    options: List<Option>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                options.forEach { option ->
                    if (!option.enabled) return@forEach
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                option.onClick()
                                onDismiss()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = option.text,
                            tint = option.iconTint(),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            option.text,
                            style = TextStyle(
                                color = option.textColor(),
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                                fontWeight = FontWeight(500)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

data class Option(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val iconTint: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    val textColor: @Composable () -> Color = { MaterialTheme.colorScheme.onSurface },
    val enabled: Boolean = true
)

enum class DialogType {
    SUCCESS,
    ERROR
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun CustomDialogPreview() {
    Scaffold {
        AlertDialog(
            onDismiss = {},
            onConfirm = {},
            title = "Example Title",
            message = "Example message",
            showCancel = false
        )
    }
}

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
            modifier = modifier
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

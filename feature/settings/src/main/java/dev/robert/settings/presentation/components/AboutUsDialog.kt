package dev.robert.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.robert.design_system.presentation.components.TDButton

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    onNavigateToGithub: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(end = 20.dp, start = 20.dp, top = 20.dp,),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "About this App",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "This is a task management app that save tasks locally on device and on the cloud. " +
                        "It is built with Jetpack Compose and Kotlin.",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(20.dp))
                TDButton(
                    onClick = onNavigateToGithub,
                    text = "View source code on Github",
                    enabled = true,
                    isLoading = false
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                ) {
                    Text(
                        text = "Developed by Robert",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // TODO: Add version from gradle
                    Text(
                        text = "Version 0.0.1-alpha",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    }
}

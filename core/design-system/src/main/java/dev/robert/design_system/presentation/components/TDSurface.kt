package dev.robert.design_system.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TDSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background, content = content)
}

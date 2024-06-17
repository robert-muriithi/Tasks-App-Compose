package dev.robert.design_system.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable

@Composable
fun TDLoader(isLoading: Boolean) {
    if (isLoading) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = { }
        )
        /*Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }*/
    }
}

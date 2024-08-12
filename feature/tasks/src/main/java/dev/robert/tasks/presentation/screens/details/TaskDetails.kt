package dev.robert.tasks.presentation.screens.details

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.robert.tasks.domain.model.TaskItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(
    onNavigateBack: () -> Unit,
    task: TaskItem
) {
    val b = task
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Text("Task Details Screen")
    }
}

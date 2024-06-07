package dev.robert.tasks.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dev.robert.tasks.presentation.navigation.TodoItem

@Composable
fun TaskDetailsScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    item: TodoItem,
) {
    val name = item.name
}

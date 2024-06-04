package dev.robert.navigation.task

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TasksNavGraph

fun NavGraphBuilder.tasksNavGraph(onNavigate: () -> Unit) {
    composable<TasksNavGraph> {
    }
}

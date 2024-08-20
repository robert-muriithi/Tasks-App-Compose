package dev.robert.tasks.presentation.utils

class Validator {
    fun validateTaskTitle(title: String): ValidatorResult = if (title.isEmpty()) {
        ValidatorResult(false, "Task title cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskDescription(description: String): ValidatorResult = if (description.isEmpty()) {
        ValidatorResult(false, "Description cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskDueDate(dueDate: String): ValidatorResult = if (dueDate.isEmpty()) {
        ValidatorResult(false, "date cannot be empty")
    } else {
        ValidatorResult(true)
    }

    fun validateTaskPriority(priority: String): ValidatorResult = if (priority.isEmpty()) {
        ValidatorResult(false, "Priority cannot be empty")
    } else {
        ValidatorResult(true)
    }
}

package xyz.teamgravity.todolist.model

import xyz.teamgravity.todolist.viewmodel.TaskSort

data class PreferencesModel(
    val taskSort: TaskSort,
    val hideCompleted: Boolean
)

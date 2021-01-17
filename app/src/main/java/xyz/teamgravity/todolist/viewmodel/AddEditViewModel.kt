package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import xyz.teamgravity.todolist.model.TaskModel

class AddEditViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle,
    private val dao: TaskDao
) : ViewModel() {

    val task = state.get<TaskModel>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
}
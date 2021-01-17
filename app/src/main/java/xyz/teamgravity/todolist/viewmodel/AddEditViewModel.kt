package xyz.teamgravity.todolist.viewmodel

import android.content.res.Resources
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.R
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

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent> {  }
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    // save button click
    fun onSaveButtonClick(res: Resources) {
        if (taskName.isBlank()) {
            showInvalidInputMessage(res.getString(R.string.field_error))
            return
        }

        // update or create
        if (task != null) {
            val updateTask = task.copy(name = taskName, important = taskImportance)
            updateTask(updateTask, res.getString(R.string.task_updated))
        } else {
            val newTask = TaskModel(name = taskName, important = taskImportance)
            insertTask(newTask, res.getString(R.string.task_created))
        }
    }

    // update task
    private fun updateTask(task: TaskModel, message: String) = viewModelScope.launch {
        dao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(message))
    }

    // insert task
    private fun insertTask(task: TaskModel, message: String) = viewModelScope.launch {
        dao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(message))
    }

    // forward invalid input event
    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputEvent(message))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputEvent(val message: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val message: String) : AddEditTaskEvent()
    }
}
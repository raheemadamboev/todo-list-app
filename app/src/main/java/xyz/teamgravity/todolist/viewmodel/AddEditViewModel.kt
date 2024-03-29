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
    companion object {
        private const val TASK = "task"
        private const val TASK_NAME = "taskName"
        private const val TASK_IMPORTANCE = "taskImportance"
    }

    val task = state.get<TaskModel>(TASK)

    var taskName = state.get<String>(TASK_NAME) ?: task?.name ?: ""
        set(value) {
            field = value
            state.set(TASK_NAME, value)
        }

    var taskImportance = state.get<Boolean>(TASK_IMPORTANCE) ?: task?.important ?: false
        set(value) {
            field = value
            state.set(TASK_IMPORTANCE, value)
        }

    // channel to send one time events
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
package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.helper.util.Preferences
import xyz.teamgravity.todolist.model.TaskModel

class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
    private val preferences: Preferences,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val SEARCH_QUERY = "searchQuery"
    }

    // query
    val query = state.getLiveData(SEARCH_QUERY, "")

    // preferences flow
    val preferencesFlow = preferences.preferenceFlow

    // channel to send one time events
    private val taskEventChannel = Channel<TaskEvent> { }
    val taskEvent = taskEventChannel.receiveAsFlow()

    // search tasks, combine everything
    private val taskFlow = combine(query.asFlow(), preferencesFlow) { query, preferencesModel ->
        Pair(query, preferencesModel)
    }.flatMapLatest { (query, preferencesModel) ->
        dao.getTasks(query, preferencesModel.hideCompleted, preferencesModel.taskSort)
    }

    // result as a live data
    val tasks = taskFlow.asLiveData()

    // update preferences
    fun onTaskSort(taskSort: TaskSort) = viewModelScope.launch {
        preferences.updateSortOrder(taskSort)
    }

    // update hide completed
    fun onHideCompleted(hideCompleted: Boolean) = viewModelScope.launch {
        preferences.updateHideCompleted(hideCompleted)
    }

    // task click
    fun onTaskClicked(task: TaskModel) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddedTaskScreen(task))
    }

    // task check
    fun onTaskChecked(task: TaskModel, isChecked: Boolean) = viewModelScope.launch {
        dao.update(task.copy(completed = isChecked))
    }

    // task swiped
    fun onTaskSwiped(task: TaskModel) = viewModelScope.launch {
        dao.delete(task)
        taskEventChannel.send(TaskEvent.ShowUndoDeleteTaskMessage(task))
    }

    // undo task delete
    fun onUndoTaskDelete(task: TaskModel) = viewModelScope.launch {
        dao.insert(task)
    }

    // add task button click
    fun onAddButtonClick() = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }

    // add edit result
    fun onAddEditResult(message: String) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.ShowAddEditResultMessage(message))
    }

    // delete all completed menu click
    fun onDeleteAllCompleted() = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToDeleteAllCompleted)
    }

    sealed class TaskEvent {
        object NavigateToAddTaskScreen : TaskEvent()
        data class NavigateToAddedTaskScreen(val task: TaskModel) : TaskEvent()
        data class ShowUndoDeleteTaskMessage(val task: TaskModel) : TaskEvent()
        data class ShowAddEditResultMessage(val message: String) : TaskEvent()
        object NavigateToDeleteAllCompleted : TaskEvent()
    }
}
package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.helper.util.Preferences
import xyz.teamgravity.todolist.model.TaskModel

class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
    private val preferences: Preferences
) : ViewModel() {

    // query
    val query = MutableStateFlow("")

    val preferencesFlow = preferences.preferenceFlow

    // search tasks
    private val taskFlow = combine(query, preferencesFlow) { query, preferencesModel ->
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
    fun onTaskClicked(task: TaskModel) {

    }

    // task check
    fun onTaskChecked(task: TaskModel, isChecked: Boolean) = viewModelScope.launch {
        dao.update(task.copy(completed = isChecked))
    }
}
package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao
) : ViewModel() {

    // query
    val query = MutableStateFlow("")

    val sortOrder = MutableStateFlow(TaskSort.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    // search tasks
    private val taskFlow = combine(query, sortOrder, hideCompleted) { query, sortOrder, hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        dao.getTasks(query, hideCompleted, sortOrder)
    }

    // result as a live data
    val tasks = taskFlow.asLiveData()
}
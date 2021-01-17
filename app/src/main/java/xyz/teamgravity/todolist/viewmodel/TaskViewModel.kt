package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao
) : ViewModel() {

    // query
    val query = MutableStateFlow("")

    // search tasks
    private val taskFlow = query.flatMapLatest {
        dao.getTasks(it)
    }

    // result as a live data
    val tasks = taskFlow.asLiveData()
}
package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao
) : ViewModel() {

    val tasks = dao.getTasks().asLiveData()
}
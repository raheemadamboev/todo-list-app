package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao
) : ViewModel() {
}
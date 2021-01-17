package xyz.teamgravity.todolist.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.injection.ApplicationScope

class DeleteViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    // confirm click delete all completed
    fun onConfirmClick() = applicationScope.launch {
        dao.deleteAllCompleted()
    }
}
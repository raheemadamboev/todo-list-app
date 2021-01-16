package xyz.teamgravity.todolist.viewmodel

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.teamgravity.todolist.injection.ApplicationScope
import xyz.teamgravity.todolist.model.TaskModel
import javax.inject.Inject
import javax.inject.Provider

class TaskCallback @Inject constructor(
    private val database: Provider<MyDatabase>,
    @ApplicationScope private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val dao = database.get().taskDao()

        applicationScope.launch {
            dao.insert(TaskModel(name = "Wash the dishes"))
            dao.insert(TaskModel(name = "Do the laundry"))
            dao.insert(TaskModel(name = "Buy groceries", important = true))
            dao.insert(TaskModel(name = "Prepare food", completed = true))
            dao.insert(TaskModel(name = "Call mom"))
            dao.insert(TaskModel(name = "Visit grandma"))
            dao.insert(TaskModel(name = "Repair my bike", completed = true))
            dao.insert(TaskModel(name = "Call Elon Musk"))
        }
    }
}
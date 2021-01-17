package xyz.teamgravity.todolist.viewmodel

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.teamgravity.todolist.helper.constants.TaskDatabase
import xyz.teamgravity.todolist.model.TaskModel

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskModel)

    @Update
    suspend fun update(task: TaskModel)

    @Delete
    suspend fun delete(task: TaskModel)

    @Query("SELECT * FROM ${TaskDatabase.TASK_TABLE} WHERE name LIKE '%' || :query || '%' ORDER BY important DESC")
    fun getTasks(query: String): Flow<List<TaskModel>>
}
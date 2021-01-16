package xyz.teamgravity.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.teamgravity.todolist.helper.constants.TaskDatabase
import java.util.*

@Entity(tableName = TaskDatabase.DATABASE_NAME)
data class TaskModel(

    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,

    val name: String,

    val important: Boolean = false,
    val completed: Boolean = false,

    val timestamp: Date = Date()
)
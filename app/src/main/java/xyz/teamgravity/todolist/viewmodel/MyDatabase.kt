package xyz.teamgravity.todolist.viewmodel

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.teamgravity.todolist.helper.constants.TaskDatabase
import xyz.teamgravity.todolist.model.TaskModel

@Database(entities = [TaskModel::class], version = TaskDatabase.DATABASE_VERSION)
abstract class MyDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}
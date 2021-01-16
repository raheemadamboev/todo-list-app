package xyz.teamgravity.todolist.injection

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import xyz.teamgravity.todolist.helper.constants.TaskDatabase
import xyz.teamgravity.todolist.viewmodel.TaskCallback
import xyz.teamgravity.todolist.viewmodel.MyDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application, callback: TaskCallback) =
        Room.databaseBuilder(app, MyDatabase::class.java, TaskDatabase.DATABASE_NAME)
            .addMigrations()
            .addCallback(callback)
            .build()

    @Singleton
    @Provides
    fun provideTaskDao(db: MyDatabase) = db.taskDao()

    @Singleton
    @Provides
    @ApplicationScope
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
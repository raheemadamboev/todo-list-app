package xyz.teamgravity.todolist.helper.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import xyz.teamgravity.todolist.model.PreferencesModel
import xyz.teamgravity.todolist.viewmodel.TaskSort
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext context: Context) {
    companion object {

        /**
         *  Preferences name
         */
        private const val PREFS = "prefs"

        /**
         * Task sort order
         */
        private val TASK_SORT = stringPreferencesKey("taskSort")

        /**
         * Task hide completed
         */
        private val HIDE_COMPLETED = booleanPreferencesKey("hideCompleted")
    }

    // create dataStore
    private val dataStore = context.createDataStore(PREFS)

    // get preferences
    val preferenceFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
        val taskSort = TaskSort.valueOf(preferences[TASK_SORT] ?: TaskSort.BY_DATE.name)

        val hideCompleted = preferences[HIDE_COMPLETED] ?: false

        PreferencesModel(taskSort, hideCompleted)
    }

    // edit preference
    suspend fun updateSortOrder(taskSort: TaskSort) {
        dataStore.edit { preferences ->
            preferences[TASK_SORT] = taskSort.name
        }
    }

    suspend fun updateHideCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[HIDE_COMPLETED] = completed
        }
    }
}
package io.github.jbarr21.appdialer.data

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class UserPreferencesRepo(private val application: Application) {

  private val Context.dataStore by preferencesDataStore(name = "user")

  private object Keys {
    val USE_HAPTIC_FEEDBACK = booleanPreferencesKey("use_haptic_feedback")
    val USE_PERSISTENT_SERVICE = booleanPreferencesKey("use_persistent_service")
  }

  val userPreferencesFlow = application.dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        Timber.e(exception, "Error reading preferences")
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }
    .map { preferences ->
      UserPreferences(
        useHapticFeedback = preferences[Keys.USE_HAPTIC_FEEDBACK] ?: true,
        usePersistentService = preferences[Keys.USE_PERSISTENT_SERVICE] ?: false
      )
    }

  suspend fun updateUseHaptipFeedback(enable: Boolean) = updatePreference(Keys.USE_HAPTIC_FEEDBACK, enable)

  suspend fun updateUsePersistentService(enable: Boolean) = updatePreference(Keys.USE_PERSISTENT_SERVICE, enable)

  suspend fun useHapticFeedback() = getValue(Keys.USE_HAPTIC_FEEDBACK, true)

  suspend fun usePersistentService() = getValue(Keys.USE_PERSISTENT_SERVICE, false)

  private suspend fun <T> updatePreference(key: Preferences.Key<T>, value: T) {
    application.dataStore.edit { preferences ->
      preferences[key] = value
    }
  }

  private suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T? = null): T {
    return application.dataStore.data
      .map { preferences -> preferences[key] ?: defaultValue }
      .first()!!
  }
}

data class UserPreferences(
  val useHapticFeedback: Boolean = true,
  val usePersistentService: Boolean = false
)
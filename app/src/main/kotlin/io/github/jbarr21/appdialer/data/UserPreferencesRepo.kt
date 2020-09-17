package io.github.jbarr21.appdialer.data

import android.app.Application
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepo @Inject constructor(private val application: Application) {

  private val dataStore = application.createDataStore(name = "user")

  private object Keys {
    val USE_HAPTIC_FEEDBACK = preferencesKey<Boolean>("use_haptic_feedback")
    val USE_PERSISTENT_SERVICE = preferencesKey<Boolean>("use_persistent_service")
  }

  val userPreferencesFlow = dataStore.data
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
    dataStore.edit { preferences ->
      preferences[key] = value
    }
  }

  private suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T? = null): T {
    return this.dataStore.data
      .map { preferences -> preferences[key] ?: defaultValue }
      .first()!!
  }
}

data class UserPreferences(
  val useHapticFeedback: Boolean = true,
  val usePersistentService: Boolean = false
)
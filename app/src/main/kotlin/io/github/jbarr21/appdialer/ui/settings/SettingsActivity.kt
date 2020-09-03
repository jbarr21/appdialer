package io.github.jbarr21.appdialer.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.PointerIcon
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import de.Maxr1998.modernpreferences.PreferencesAdapter
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.util.AppIconFetcher
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

  @Inject
  lateinit var appIcon: AppIconFetcher

  private val preferencesAdapter by lazy { createPreferencesAdapter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    println(appIcon.toString())

    RecyclerView(this).apply {
      layoutManager = LinearLayoutManager(this@SettingsActivity)
      adapter = preferencesAdapter
      setContentView(this)
    }

    // Restore adapter state from saved state
    savedInstanceState?.getParcelable<PreferencesAdapter.SavedState>("adapter")
      ?.let(preferencesAdapter::loadSavedState)
  }

  // Save the adapter state as a parcelable into the Android-managed instance state
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelable("adapter", preferencesAdapter.getSavedState())
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.settings, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> onBackPressed()
      R.id.refresh -> ProcessPhoenix.triggerRebirth(this)
      else -> super.onOptionsItemSelected(item)
    }.run { true }
  }

  override fun onBackPressed() {
    if (!preferencesAdapter.goBack())
      super.onBackPressed()
  }

  override fun onDestroy() {
    preferencesAdapter.onScreenChangeListener = null
    super.onDestroy()
  }

  private fun createPreferencesAdapter(context: Context): PreferencesAdapter {
    val preferenceScreen = Settings.createScreen(context.applicationContext)
    return PreferencesAdapter(preferenceScreen)
  }
}
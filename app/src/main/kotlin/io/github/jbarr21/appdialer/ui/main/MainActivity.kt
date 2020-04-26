package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.NO_ID
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.ui.main.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerViewModel
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.ui.settings.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, ModalBottomSheetDialogFragment.Listener {

  companion object {
    private const val NUM_APP_COLUMNS = 4
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main

  private val mainScope: MainScope by lazy { AppDialerApplication.component(this).mainScope(this) }
  private val appStream: AppStream by lazy { mainScope.appStream() }
  private val appAdapter by lazy { mainScope.appAdapter() }
  private val appRepo by lazy { mainScope.appRepo() }
  private val dialerViewModel by lazy { ViewModelProviders.of(this).get(DialerViewModel::class.java) }
  private val queryStream: QueryStream by lazy { mainScope.queryStream() }
  private val launcherApps: LauncherApps by lazy { getSystemService<LauncherApps>()!! }

  private val swipeRefresh by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) }
  private val appGrid by lazy { findViewById<RecyclerView>(R.id.app_grid) }
  private val dialer by lazy { findViewById<RecyclerView>(R.id.dialer) }

  var longPressedApp: App? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupAppGrid()
    setupDialerButtons(mainScope.dialerAdapter())

    mainScope.appClickStream()
      .clicks()
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe { app ->
        run {
          launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
          finish()
          queryStream.setQuery(emptyList())
        }
      }

    mainScope.appClickStream()
      .longClicks()
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe {app ->
        run {
          // TODO: fix how to pass this (use another lib?)
          longPressedApp = app
          ModalBottomSheetDialogFragment.Builder()
            .header(app.label)
            .add(R.menu.app_details)
            .build()
            .show(supportFragmentManager, app.uri.toString())
        }
      }

    mainScope.queryStream().longClicks()
      .filter { it.isClearButton }
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe {
        when {
          it.isClearButton -> startActivity(Intent(this, SettingsActivity::class.java))
          it.isInfoButton -> displayAppCount()
        }
      }

    swipeRefresh.refreshes()
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe {
        appRepo.loadApps(useCache = false)
      }

    mainScope.queryStream().query()
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe { query ->
        if (query.isNullOrEmpty()) {
          clearQuery()
        } else {
          queryApps(query)
        }
      }

    // TODO: add empty currentApps and progress currentApps
    appStream.allApps()
      .filter { queryStream.currentQuery().isEmpty() }
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(AndroidLifecycleScopeProvider.from(this))
      .subscribe { apps ->
        apps.forEach {
          dialerViewModel.trie.add(it.label, it)
        }
        appAdapter.submitList(apps)
      }

    appRepo.loadApps()
  }

  private fun setupAppGrid() {
    appGrid.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(this@MainActivity,
        NUM_APP_COLUMNS
      )
      (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
      adapter = appAdapter
    }
  }

  private fun setupDialerButtons(dialerAdapter: DialerAdapter) {
    dialer.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(this@MainActivity, 3)
      adapter = dialerAdapter
    }
    dialerAdapter.submitList(mainScope.dialerLabels())
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.app_grid_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId ?: NO_ID) {
      R.id.app_count -> displayAppCount()
      R.id.refresh -> appRepo.loadApps(useCache = false)
      R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
      else -> return super.onOptionsItemSelected(item)
    }
    return true
  }

  private fun queryApps(buttons: List<DialerButton>) {
    dialerViewModel.query.apply {
      clear()
      addAll(buttons)
    }
    // TODO: update UI or use LiveData/RxJava
    dialerViewModel.trie.predictWord(dialerViewModel.query.map { it.letters[0].toString() }.joinToString(separator = ""))
      .sortedBy { it.label.toLowerCase() }
      .also {
        val query = dialerViewModel.query.map { app -> app.digit }.joinToString(separator = "")
        println("suggestions for \"$query\": ${it.take(5).map { it.label }.toList()}")
        appAdapter.submitList(it)
      }
  }

  private fun clearQuery() {
    dialerViewModel.query.clear()
    appAdapter.submitList(appStream.currentApps()) {
      appGrid.scrollToPosition(0)
    }
  }

  private fun displayAppCount() {
    Toast.makeText(this, "Found ${appStream.currentApps().size} apps", Toast.LENGTH_SHORT).show()
  }

  override fun onModalOptionSelected(tag: String?, option: Option) = mainScope.modalFragmentListener().onModalOptionSelected(tag, option)
}

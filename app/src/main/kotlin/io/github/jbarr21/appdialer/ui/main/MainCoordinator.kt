package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uber.autodispose.lifecycle.autoDisposable
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.ui.BaseCoordinator
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.ui.main.apps.AppAdapter
import io.github.jbarr21.appdialer.ui.main.apps.AppClickStream
import io.github.jbarr21.appdialer.ui.main.apps.ModalFragmentListener
import io.github.jbarr21.appdialer.ui.main.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerViewModel
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.ui.settings.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class MainCoordinator(
  private val activity: MainActivity,
  private val activityLauncher: ActivityLauncher,
  private val appClickStream: AppClickStream,
  private val appStream: AppStream,
  private val appAdapter: AppAdapter,
  private val appRepo: AppRepo,
  private val dialerAdapter: DialerAdapter,
  private val dialerViewModel: DialerViewModel,
  private val fragmentManager: FragmentManager,
  private val launcherApps: LauncherApps,
  private val modalFragmentListener: ModalFragmentListener,
  private val queryStream: QueryStream
) : BaseCoordinator(), CoroutineScope, ModalBottomSheetDialogFragment.Listener {

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main

  private val swipeRefresh by lazy { activity.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) }
  private val appGrid by lazy { activity.findViewById<RecyclerView>(R.id.app_grid) }
  private val dialer by lazy { activity.findViewById<RecyclerView>(R.id.dialer) }

  private var longPressedApp: App? = null

  override fun attach(view: View) {
    super.attach(view)
    setupAppGrid()
    setupDialerButtons(dialerAdapter)

    appClickStream.clicks()
      .autoDisposable(this)
      .subscribe { app ->
        run {
          launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
          activity.finish()
          queryStream.setQuery(emptyList())
        }
      }

    appClickStream.longClicks()
      .autoDisposable(this)
      .subscribe {app ->
        run {
          // TODO: fix how to pass this (use another lib?)
          longPressedApp = app
          ModalBottomSheetDialogFragment.Builder()
            .header(app.label)
            .add(R.menu.app_details)
            .build()
            .show(fragmentManager, app.uri.toString())
        }
      }

    queryStream.longClicks()
      .autoDisposable(this)
      .subscribe {
        when {
          it.isClearButton -> activityLauncher.startActivity(Intent(activity, SettingsActivity::class.java))
          it.isInfoButton -> displayAppCount()
        }
      }

    swipeRefresh.refreshes()
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(this)
      .subscribe {
        appRepo.loadApps(useCache = false)
      }

    queryStream.query()
      .observeOn(AndroidSchedulers.mainThread())
      .autoDisposable(this)
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
      .autoDisposable(this)
      .subscribe { apps ->
        apps.forEach {
          dialerViewModel.trie.add(it.label, it)
        }
        appAdapter.submitList(apps)
      }

    appRepo.loadApps()
  }

  override fun detach(view: View) {

    super.detach(view)
  }

  private fun setupAppGrid() {
    appGrid.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(activity,
        NUM_APP_COLUMNS
      )
      (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
      adapter = appAdapter
    }
  }

  private fun setupDialerButtons(dialerAdapter: DialerAdapter) {
    dialer.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(activity, 3)
      adapter = dialerAdapter
    }
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
    Toast.makeText(activity, "Found ${appStream.currentApps().size} apps", Toast.LENGTH_SHORT).show()
  }

  override fun onModalOptionSelected(tag: String?, option: Option) = modalFragmentListener.onModalOptionSelected(tag, option)

  companion object {
    private const val NUM_APP_COLUMNS = 4
  }
}

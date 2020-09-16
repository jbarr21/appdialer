package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import dagger.hilt.android.scopes.ActivityScoped
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.databinding.ActivityMainBinding
import io.github.jbarr21.appdialer.ui.BaseCoordinator
import io.github.jbarr21.appdialer.ui.main.apps.AppAdapter
import io.github.jbarr21.appdialer.ui.main.apps.AppClickStream
import io.github.jbarr21.appdialer.ui.main.apps.ModalFragmentListener
import io.github.jbarr21.appdialer.ui.main.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerViewModel
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.ui.settings.SettingsActivity
import io.github.jbarr21.appdialer.util.ActivityLauncher
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.material.slides
import ru.ldralighieri.corbind.swiperefreshlayout.refreshes
import javax.inject.Inject

@ActivityScoped
class MainCoordinator @Inject constructor(
  private val activity: FragmentActivity,
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
) : BaseCoordinator(activity.lifecycleScope), ModalBottomSheetDialogFragment.Listener {

  private lateinit var viewBinding: ActivityMainBinding

  private var longPressedApp: App? = null

  override fun attach(view: View) {
    super.attach(view)
    viewBinding = ActivityMainBinding.bind(view)
    setupAppGrid()
    setupDialerButtons(dialerAdapter)

    appClickStream.clicks()
      .onEach { launchApp(it) }
      .launchIn(this)

    appClickStream.longClicks()
      .onEach { showAppOptionsModal(it) }
      .launchIn(this)

    queryStream.query()
      .onEach { handleQuery(it) }
      .launchIn(this)

    queryStream.longClicks()
      .onEach { onDialerLongClick(it) }
      .launchIn(this)

    // TODO: add empty currentApps and progress currentApps
    appStream.allApps()
      .filter { queryStream.currentQuery().isEmpty() }
      .onEach { handleAppListUpdate(it) }
      .launchIn(this)

    viewBinding.swipeRefresh.refreshes()
      .onEach { onPullToRefresh() }
      .launchIn(this)

    appRepo.loadApps() {
      viewBinding.swipeRefresh.isRefreshing = false
    }
  }

  private fun setupAppGrid() {
    viewBinding.appGrid.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(activity, NUM_APP_COLUMNS)
      adapter = appAdapter
    }
  }

  private fun setupDialerButtons(dialerAdapter: DialerAdapter) {
    viewBinding.bottomSheet.dialer.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(activity, 3)
      adapter = dialerAdapter
    }

    val bottomSheet = viewBinding.bottomSheet.bottomSheet
    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
    bottomSheet.slides()
      .onEach { slideOffset ->
        viewBinding.bottomSheet.dialer.apply {
          translationY = (1 - slideOffset) * resources.getDimension(R.dimen.dialer_sheet_peek_height)
        }
      }
      .launchIn(this)
  }

  private fun queryApps(buttons: List<DialerButton>) {
    dialerViewModel.query.apply {
      clear()
      addAll(buttons)
    }
    // TODO: update UI or use Flow
    dialerViewModel.trie.predictWord(dialerViewModel.query.map { it.letters[0].toString() }
      .joinToString(separator = ""))
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
      viewBinding.appGrid.scrollToPosition(0)
    }
  }

  private fun handleQuery(query: List<DialerButton>) {
    if (query.isNullOrEmpty()) {
      clearQuery()
    } else {
      queryApps(query)
    }
  }

  private fun handleAppListUpdate(apps: List<App>) {
    apps.forEach {
      dialerViewModel.trie.add(it.label, it)
    }
    appAdapter.submitList(apps)
  }

  private fun launchApp(app: App) {
    launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
    queryStream.setQuery(emptyList(), this)
    activity.finishAndRemoveTask()
  }

  private fun onDialerLongClick(button: DialerButton) {
    when {
      button.isClearButton -> activityLauncher.startActivity(Intent(activity, SettingsActivity::class.java))
      button.isInfoButton -> displayAppCount()
    }
  }

  private fun onPullToRefresh() {
    appRepo.loadApps(useCache = false) {
      viewBinding.swipeRefresh.isRefreshing = false
    }
  }

  private fun showAppOptionsModal(app: App) {
    longPressedApp = app
    ModalBottomSheetDialogFragment.Builder()
      .header(app.label)
      .add(R.menu.app_details)
      .build()
      .show(fragmentManager, app.iconUri.toString())
  }

  private fun displayAppCount() {
    Snackbar.make(viewBinding.coordinator, "Found ${appStream.currentApps().size} apps", LENGTH_SHORT)
      .setAnchorView(viewBinding.bottomSheet.dialerSheetHandle)
      .show()
  }

  override fun onModalOptionSelected(tag: String?, option: Option) = modalFragmentListener.onModalOptionSelected(
    tag,
    option
  )

  companion object {
    private const val NUM_APP_COLUMNS = 4
  }
}

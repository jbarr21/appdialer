package io.github.jbarr21.appdialer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.NO_ID
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.ui.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.dialer.DialerViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
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

  private val appGrid by lazy { findViewById<RecyclerView>(R.id.app_grid) }
  private val dialer by lazy { findViewById<RecyclerView>(R.id.dialer) }

  var longPressedApp: App? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.tag("JIM").d("Activity created")
    setContentView(R.layout.activity_main)
    setupAppGrid()
    setupDialerButtons(mainScope.dialerAdapter())

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
      layoutManager = GridLayoutManager(this@MainActivity, NUM_APP_COLUMNS)
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
    return when (item?.itemId ?: NO_ID) {
      R.id.app_count -> {
        val appCount = appStream.currentApps().size
        Toast.makeText(this@MainActivity, "Found $appCount apps", Toast.LENGTH_SHORT).show()
        true
      }
      R.id.refresh -> {
        appRepo.loadApps(useCache = false)
        true
      }
      else -> super.onOptionsItemSelected(item)
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

  override fun onModalOptionSelected(tag: String?, option: Option) = mainScope.modalFragmentListener().onModalOptionSelected(tag, option)
}

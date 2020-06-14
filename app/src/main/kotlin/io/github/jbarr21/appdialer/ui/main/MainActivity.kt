package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.squareup.coordinators.Coordinators
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ModalBottomSheetDialogFragment.Listener {

  private var mainCoordinator: MainCoordinator? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Coordinators.bind(findViewById(android.R.id.content)) { view ->
      mainCoordinator = AppDialerApplication.component(this)
        .mainScope(this@MainActivity, view as ViewGroup)
        .coordinator()
      return@bind mainCoordinator
    }
  }

  override fun onModalOptionSelected(tag: String?, option: Option) {
    mainCoordinator?.onModalOptionSelected(tag, option)
  }
}

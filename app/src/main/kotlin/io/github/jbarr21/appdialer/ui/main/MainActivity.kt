package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.squareup.coordinators.Coordinators
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.ui.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), ModalBottomSheetDialogFragment.Listener {

  @Inject
  lateinit var mainCoordinator: MainCoordinator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val rootView = (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
    Coordinators.bind(rootView) { mainCoordinator }
  }

  override fun onModalOptionSelected(tag: String?, option: Option) {
    mainCoordinator.onModalOptionSelected(tag, option)
  }
}

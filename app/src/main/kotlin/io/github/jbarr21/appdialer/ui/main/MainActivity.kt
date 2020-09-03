package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.squareup.coordinators.Coordinators
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ModalBottomSheetDialogFragment.Listener {

  @Inject
  lateinit var mainCoordinator: MainCoordinator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Coordinators.bind(findViewById(android.R.id.content)) {
      return@bind mainCoordinator
    }
  }

  override fun onModalOptionSelected(tag: String?, option: Option) {
    mainCoordinator?.onModalOptionSelected(tag, option)
  }
}

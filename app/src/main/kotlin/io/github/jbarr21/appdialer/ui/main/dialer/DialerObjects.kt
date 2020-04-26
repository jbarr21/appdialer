package io.github.jbarr21.appdialer.ui.main.dialer

import android.app.Activity
import android.app.Application
import android.os.Vibrator
import com.google.common.base.Optional
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.util.vibrateIfAble

interface DialerObjects {

  fun dialerAdapter(
    activity: Activity,
    application: Application,
    appStream: AppStream,
    dialerLabels: List<DialerButton>,
    queryStream: QueryStream,
    vibrator: Optional<Vibrator>
  ) = DialerAdapter(
    DialerButtonDiffCallback(),
    dialerLabels,
    { button ->
      if (button.isClearButton || button.isInfoButton) {
        vibrator.orNull()?.vibrateIfAble(application)
        queryStream.longClick(button)
      }
    },
    { button ->
      if (appStream.currentApps().isNotEmpty()) {
        vibrator.orNull()?.vibrateIfAble(application)
        if (button.isClearButton) {
          queryStream.setQuery(emptyList())
        } else {
          queryStream.setQuery(queryStream.currentQuery() + button)
        }
      }
    }
  )

  fun dialerLabels(): List<DialerButton> {
    return listOf(DialerButton(label = "CLEAR *")) +
      keyMappings().map { (digit, letters) ->
        DialerButton(
          digit = digit,
          letters = letters
        )
      }.toList()
  }

  fun keyMappings() = mapOf(2 to "abc", 3 to "def", 4 to "ghi", 5 to "jkl", 6 to "mno", 7 to "pqrs", 8 to "tuv", 9 to "wxyz")

  fun queryStream(): QueryStream
}
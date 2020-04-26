package io.github.jbarr21.appdialer.ui.main.apps

import androidx.recyclerview.widget.DiffUtil
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo

interface AppObjects {

  fun appAdapter(): AppAdapter

  fun appClickStream(): AppClickStream

  fun appDiffCallback(): DiffUtil.ItemCallback<App> = AppDiffCallback()

  fun appRepo(): AppRepo
}

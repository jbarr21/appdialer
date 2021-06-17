package io.github.jbarr21.appdialer.ui.main.apps

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.util.ActivityLauncher

@InstallIn(ViewModelComponent::class)
@Module
object AppGridModule {

  @Provides
  fun appLongClickActions(activityLauncher: ActivityLauncher): List<SimpleListItem<App>> {
    return listOf(
      SimpleListItem("Uninstall", iconRes = R.drawable.ic_delete_black_24dp, action = {
        activityLauncher.uninstallApp(it)
      }),
      SimpleListItem("App Info", iconRes = R.drawable.ic_info_black_24dp, action = {
        activityLauncher.startAppDetails(it)
      }),
      SimpleListItem("Play Store", iconRes = R.drawable.ic_local_grocery_store_black_24dp, action = {
        activityLauncher.startPlayStore(it)
      })
    )
  }
}

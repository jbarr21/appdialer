package io.github.jbarr21.appdialer.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.data.UserPreferences
import io.github.jbarr21.appdialer.ui.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

  @Inject lateinit var viewModelFactory: SettingsViewModel.Factory
  @Inject lateinit var settingsData: List<SimpleListItem<Any>>

  private val viewModel: SettingsViewModel by viewModels { viewModelFactory }
  private val onNavIconPressed = { finish() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.userPreferences.observe(this) {
      setContent {
        SettingsScreen(onNavIconPressed = onNavIconPressed, userPreferences = it)
      }
    }
  }

  @Composable
  fun SettingsScreen(
    onNavIconPressed: () -> Unit,
    userPreferences: UserPreferences
  ) {
    AppTheme {
      Scaffold(
        topBar = { TopBar(onNavIconPressed = onNavIconPressed) },
        bodyContent = {
          Surface(modifier = Modifier.fillMaxSize()) {
            Column {
              SettingsGroup("General")
              SettingsItem(
                listItem = settingsData[0],
                checked = userPreferences.useHapticFeedback,
                onCheckedChange = { viewModel.updateUseHaptipFeedback(it) }
              )
              SettingsItem(
                listItem = settingsData[1],
                checked = userPreferences.usePersistentService,
                onCheckedChange = { viewModel.updateUsePersistentService(it) }
              )
            }
          }
        }
      )
    }
  }

  @Composable
  fun TopBar(onNavIconPressed: () -> Unit) {
    TopAppBar(
      title = { Text("AppDialer Settings") },
      navigationIcon = {
        Image(
          asset = vectorResource(id = R.drawable.ic_back),
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onNavIconPressed)
        )
      }
    )
  }

  @Composable
  fun SettingsGroup(title: String) {
    Text(
      title,
      style = MaterialTheme.typography.button,
      color = MaterialTheme.colors.secondaryVariant,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 72.dp)
        .padding(top = 16.dp)
    )
  }

  @Composable
  fun SettingsItem(listItem: SimpleListItem<Any>, checked: Boolean, onCheckedChange: (Boolean) -> Unit = { }) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable(onClick = { onCheckedChange(!checked) }).padding(vertical = 16.dp)
    ) {
      Image(
        asset = vectorResource(id = listItem.iconRes),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
        modifier = Modifier.padding(horizontal = 24.dp)
      )
      Column(modifier = Modifier.weight(1f, fill = true)) {
        Text(listItem.label, style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium))
        Text(listItem.description.orEmpty(), style = MaterialTheme.typography.body2)
      }
      Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }
  }

  @Preview
  @Composable
  fun DefaultPreview() {
    Column {
      TopBar(onNavIconPressed = {})

      SettingsGroup(title = "General")

      repeat(3) {
        SettingsItem(
          listItem = SimpleListItem(
            label = "Setting title $it",
            description = "A description for the use of the setting $it",
            iconRes = R.drawable.ic_vibration
          ),
          checked = it % 2 == 0
        )
      }
    }
  }
}
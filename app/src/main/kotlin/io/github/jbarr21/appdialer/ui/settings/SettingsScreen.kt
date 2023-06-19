package io.github.jbarr21.appdialer.ui.settings

import androidx.compose.foundation.Image
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.common.AppDialerTopBar

@Composable
fun SettingsScreen(
  viewModel: SettingsViewModel = viewModel(),
  navController: NavController
) {
  Scaffold(topBar = { AppDialerTopBar(title = "AppDialer Settings", navController = navController) }) {
    Surface(modifier = Modifier.fillMaxSize()) {
      Column {
        SettingsGroup("General")
        SettingsItem(
          listItem = viewModel.settingsData[0],
          checked = viewModel.userPreferences.useHapticFeedback,
          onCheckedChange = { viewModel.updateUseHaptipFeedback(it) }
        )
        SettingsItem(
          listItem = viewModel.settingsData[1],
          checked = viewModel.userPreferences.usePersistentService,
          onCheckedChange = { viewModel.updateUsePersistentService(it) }
        )
      }
    }
  }
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
fun SettingsItem(listItem: SimpleListItem<Unit>, checked: Boolean, onCheckedChange: (Boolean) -> Unit = { }) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable(onClick = { onCheckedChange(!checked) })
      .padding(vertical = 16.dp)
  ) {
    Image(
      painter = painterResource(id = listItem.iconRes),
      colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
      contentDescription = null,
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

@Preview(name = "Settings screen", group = "Settings")
@Composable
fun SettingsScreenPreview() {
  AppTheme(darkTheme = true) {
    Surface {
      Column {
        AppDialerTopBar(title = "AppDialer Settings", navController = NavController(LocalContext.current))

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
}

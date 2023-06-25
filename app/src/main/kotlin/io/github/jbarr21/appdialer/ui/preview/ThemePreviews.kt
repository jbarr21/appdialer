package io.github.jbarr21.appdialer.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark", group = "Themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light", group = "Themes", uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class ThemePreviews
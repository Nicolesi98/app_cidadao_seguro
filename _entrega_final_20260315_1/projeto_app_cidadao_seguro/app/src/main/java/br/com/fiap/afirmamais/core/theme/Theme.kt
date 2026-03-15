package br.com.fiap.afirmamais.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PurplePrimary,
    secondary = PinkPrimary,
    background = BackgroundLight,
    surface = SurfaceLight,
)

private val DarkColors = darkColorScheme(
    primary = PurplePrimary,
    secondary = PinkPrimary,
)

@Composable
fun AfirmaTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        content = content,
    )
}
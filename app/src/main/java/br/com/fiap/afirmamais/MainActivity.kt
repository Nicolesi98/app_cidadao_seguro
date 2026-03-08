package br.com.fiap.afirmamais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import br.com.fiap.afirmamais.core.theme.AfirmaTheme
import br.com.fiap.afirmamais.presentation.navigation.AfirmaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appContainer = (application as AfirmaApplication).appContainer

        setContent {
            AfirmaTheme {
                AfirmaApp(appContainer = appContainer)
            }
        }
    }
}
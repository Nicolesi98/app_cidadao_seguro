package br.com.fiap.afirmamais

import android.app.Application
import br.com.fiap.afirmamais.di.AppContainer

class AfirmaApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}
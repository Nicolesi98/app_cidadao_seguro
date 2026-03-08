package br.com.fiap.afirmamais.di

import android.content.Context
import br.com.fiap.afirmamais.data.local.PreferencesStorage
import br.com.fiap.afirmamais.data.remote.AssetJobsDataSource
import br.com.fiap.afirmamais.data.remote.JobsRemoteDataSource
import br.com.fiap.afirmamais.data.repository.AuthRepositoryImpl
import br.com.fiap.afirmamais.data.repository.JobRepositoryImpl
import br.com.fiap.afirmamais.data.repository.UserDataRepositoryImpl
import br.com.fiap.afirmamais.domain.repository.AuthRepository
import br.com.fiap.afirmamais.domain.repository.JobRepository
import br.com.fiap.afirmamais.domain.repository.UserDataRepository
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

class AppContainer(context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val httpClient = OkHttpClient.Builder().build()
    private val storage = PreferencesStorage(context = context, json = json)
    private val remoteDataSource = JobsRemoteDataSource(client = httpClient, json = json)
    private val assetDataSource = AssetJobsDataSource(context = context, json = json)

    val authRepository: AuthRepository = AuthRepositoryImpl(storage = storage)
    val jobRepository: JobRepository = JobRepositoryImpl(
        remoteDataSource = remoteDataSource,
        assetDataSource = assetDataSource,
    )
    val userDataRepository: UserDataRepository = UserDataRepositoryImpl(storage = storage)
}
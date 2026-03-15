package br.com.fiap.afirmamais.data.repository

import br.com.fiap.afirmamais.data.local.LocalJobsCacheDataSource
import br.com.fiap.afirmamais.data.remote.JobsRemoteDataSource
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.domain.repository.JobRepository

class JobRepositoryImpl(
    private val remoteDataSource: JobsRemoteDataSource,
    private val localCacheDataSource: LocalJobsCacheDataSource,
) : JobRepository {

    override suspend fun fetchAllJobs(): List<Job> {
        val remoteJobs = runCatching { remoteDataSource.fetchAllJobs() }.getOrNull()
        if (!remoteJobs.isNullOrEmpty()) {
            runCatching { localCacheDataSource.saveAllJobs(remoteJobs) }
            return remoteJobs
        }

        return runCatching { localCacheDataSource.fetchAllJobs() }.getOrElse { emptyList() }
    }

    override suspend fun fetchJobById(id: Int): Job? {
        val remoteJob = runCatching { remoteDataSource.fetchJobById(id) }.getOrNull()
        if (remoteJob != null) {
            runCatching { localCacheDataSource.upsertJob(remoteJob) }
            return remoteJob
        }

        return runCatching { localCacheDataSource.fetchJobById(id) }.getOrNull()
    }
}

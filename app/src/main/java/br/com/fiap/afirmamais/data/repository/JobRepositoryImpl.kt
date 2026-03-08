package br.com.fiap.afirmamais.data.repository

import br.com.fiap.afirmamais.data.remote.AssetJobsDataSource
import br.com.fiap.afirmamais.data.remote.JobsRemoteDataSource
import br.com.fiap.afirmamais.domain.model.Job
import br.com.fiap.afirmamais.domain.repository.JobRepository

class JobRepositoryImpl(
    private val remoteDataSource: JobsRemoteDataSource,
    private val assetDataSource: AssetJobsDataSource,
) : JobRepository {

    override suspend fun fetchAllJobs(): List<Job> {
        val remoteJobs = runCatching { remoteDataSource.fetchAllJobs() }.getOrNull()
        if (!remoteJobs.isNullOrEmpty()) {
            return remoteJobs
        }

        return runCatching { assetDataSource.fetchAllJobs() }.getOrElse { emptyList() }
    }

    override suspend fun fetchJobById(id: Int): Job? {
        val remoteJob = runCatching { remoteDataSource.fetchJobById(id) }.getOrNull()
        if (remoteJob != null) {
            return remoteJob
        }

        return fetchAllJobs().firstOrNull { it.id == id }
    }
}
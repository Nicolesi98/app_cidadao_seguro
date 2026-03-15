package br.com.fiap.afirmamais.domain.repository

import br.com.fiap.afirmamais.domain.model.Job

interface JobRepository {
    suspend fun fetchAllJobs(): List<Job>
    suspend fun fetchJobById(id: Int): Job?
}
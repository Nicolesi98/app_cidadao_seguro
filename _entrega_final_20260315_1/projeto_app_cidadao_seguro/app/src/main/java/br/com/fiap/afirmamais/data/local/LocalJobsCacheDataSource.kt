package br.com.fiap.afirmamais.data.local

import android.content.Context
import br.com.fiap.afirmamais.domain.model.Job
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalJobsCacheDataSource(
    context: Context,
    private val json: Json,
) {
    private val cacheFile = File(context.filesDir, CACHE_FILE_NAME)

    fun fetchAllJobs(): List<Job> {
        if (!cacheFile.exists()) return emptyList()
        val raw = runCatching { cacheFile.readText() }.getOrElse { return emptyList() }
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString<List<Job>>(raw) }.getOrElse { emptyList() }
    }

    fun fetchJobById(id: Int): Job? {
        return fetchAllJobs().firstOrNull { it.id == id }
    }

    fun saveAllJobs(jobs: List<Job>) {
        if (jobs.isEmpty()) return
        val payload = json.encodeToString(jobs)
        cacheFile.writeText(payload)
    }

    fun upsertJob(job: Job) {
        val current = fetchAllJobs().toMutableList()
        val index = current.indexOfFirst { it.id == job.id }
        if (index >= 0) {
            current[index] = job
        } else {
            current.add(job)
        }
        saveAllJobs(current)
    }

    private companion object {
        const val CACHE_FILE_NAME = "jobs_cache.json"
    }
}

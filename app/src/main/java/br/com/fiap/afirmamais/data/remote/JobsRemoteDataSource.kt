package br.com.fiap.afirmamais.data.remote

import br.com.fiap.afirmamais.domain.model.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.OkHttpClient
import okhttp3.Request

class JobsRemoteDataSource(
    private val client: OkHttpClient,
    private val json: Json,
) {

    suspend fun fetchAllJobs(): List<Job> = withContext(Dispatchers.IO) {
        val jobs = mutableListOf<Job>()
        var nextUrl: String? = BASE_URL

        while (nextUrl != null) {
            val payload = request(nextUrl)
            when (payload) {
                is JsonArray -> {
                    jobs += payload.mapNotNull(::decodeJob)
                    nextUrl = null
                }

                is JsonObject -> {
                    val dataArray = payload["data"] as? JsonArray
                    jobs += dataArray.orEmpty().mapNotNull(::decodeJob)
                    nextUrl = payload.extractNext()
                }

                else -> {
                    nextUrl = null
                }
            }
        }

        jobs
    }

    suspend fun fetchJobById(id: Int): Job? = withContext(Dispatchers.IO) {
        val payload = request("$BASE_URL/$id")
        when (payload) {
            is JsonObject -> {
                val wrapped = payload["data"]
                when {
                    wrapped is JsonObject -> decodeJob(wrapped)
                    payload["id"] != null -> decodeJob(payload)
                    else -> null
                }
            }

            else -> null
        }
    }

    private fun request(url: String): JsonElement {
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("Request failed with code ${response.code}")
            }
            val body = response.body?.string().orEmpty()
            return json.parseToJsonElement(body)
        }
    }

    private fun JsonObject.extractNext(): String? {
        val links = this["links"] as? JsonObject ?: return null
        val next = links["next"]
        return (next as? JsonPrimitive)?.contentOrNull
    }

    private fun decodeJob(element: JsonElement): Job? {
        return try {
            json.decodeFromJsonElement<Job>(element)
        } catch (_: Exception) {
            null
        }
    }

    private companion object {
        const val BASE_URL = "https://apis.codante.io/api/job-board/jobs"
    }
}

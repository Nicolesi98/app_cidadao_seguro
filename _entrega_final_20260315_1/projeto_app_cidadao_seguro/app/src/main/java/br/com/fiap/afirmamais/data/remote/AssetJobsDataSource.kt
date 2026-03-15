package br.com.fiap.afirmamais.data.remote

import android.content.Context
import br.com.fiap.afirmamais.domain.model.Job
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class AssetJobsDataSource(
    private val context: Context,
    private val json: Json,
) {

    fun fetchAllJobs(): List<Job> {
        val raw = context.assets.open(ASSET_FILE).bufferedReader().use { it.readText() }
        val payload = json.parseToJsonElement(raw)

        if (payload is JsonArray) {
            return payload.mapNotNull(::decodeJob)
        }

        if (payload is JsonObject) {
            val data = payload["data"] as? JsonArray
            return data.orEmpty().mapNotNull(::decodeJob)
        }

        return emptyList()
    }

    private fun decodeJob(element: kotlinx.serialization.json.JsonElement): Job? {
        return try {
            json.decodeFromJsonElement<Job>(element)
        } catch (_: Exception) {
            null
        }
    }

    private companion object {
        const val ASSET_FILE = "jobs.json"
    }
}
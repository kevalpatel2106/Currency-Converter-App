package com.kevalpatel2106.fxratesample.repo.network

import com.squareup.moshi.*

class HashMapMoshiAdapter : JsonAdapter<Map<String, Double>>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Map<String, Double>? {
        val result = hashMapOf<String, Double>()

        reader.beginObject()
        while (reader.hasNext()) {
            result[reader.nextName()] = reader.nextDouble()
        }
        reader.endObject()
        return result
    }

    // Not implemented. Implement in future if needed.
    @ToJson
    override fun toJson(writer: JsonWriter, value: Map<String, Double>?) = Unit
}

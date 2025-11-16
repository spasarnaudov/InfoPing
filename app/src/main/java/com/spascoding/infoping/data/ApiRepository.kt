package com.spascoding.infoping.data

import com.spascoding.infoping.presentation.InfoPingState
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ApiRepository {
    private val client = OkHttpClient()

    fun testUrl(url: String): InfoPingState {
        return try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            val body = response.body?.string() ?: ""

            val prettyJson = try {
                JSONObject(body).toString(4)
            } catch (e: Exception) {
                "Not JSON or cannot parse"
            }

            InfoPingState(
                url = url,
                status = "${response.code} ${response.message}",
                headers = response.headers.toString(),
                json = prettyJson,
                error = ""
            )

        } catch (e: Exception) {
            InfoPingState(
                url = url,
                error = e.toString()
            )
        }
    }
}
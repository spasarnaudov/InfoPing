package com.spascoding.infoping

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@Composable
fun ApiTesterSimpleScreen() {

    var url by remember { mutableStateOf("http://192.168.0.6/myapi") }

    var resultStatus by remember { mutableStateOf("") }
    var resultHeaders by remember { mutableStateOf("") }
    var resultJson by remember { mutableStateOf("") }
    var resultError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val client = remember { OkHttpClient() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("API URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    try {
                        val request = Request.Builder().url(url).build()
                        val response = client.newCall(request).execute()

                        resultStatus = "${response.code} ${response.message}"

                        // Headers
                        resultHeaders = response.headers.toString()

                        // Try to format JSON
                        resultJson = try {
                            val raw = response.body?.string() ?: ""
                            val json = JSONObject(raw)
                            json.toString(4) // pretty format
                        } catch (e: Exception) {
                            "Not JSON or can't parse"
                        }

                        resultError = ""

                    } catch (e: Exception) {
                        resultError = e.toString()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test")
        }

        Spacer(Modifier.height(20.dp))

        if (resultError.isNotEmpty()) {
            Text("Error:", style = MaterialTheme.typography.titleMedium)
            Text(resultError, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
        }

        Text("HTTP Status:", style = MaterialTheme.typography.titleMedium)
        Text(resultStatus)
        Spacer(Modifier.height(16.dp))

        Text("Headers:", style = MaterialTheme.typography.titleMedium)
        Text(resultHeaders)
        Spacer(Modifier.height(16.dp))

        Text("JSON:", style = MaterialTheme.typography.titleMedium)
        Text(resultJson)
    }
}
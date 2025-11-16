package com.spascoding.infoping.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun InfoPingScreen(
    vm: InfoPingViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        OutlinedTextField(
            value = state.url,
            onValueChange = { vm.updateUrl(it) },
            label = { Text("API URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { vm.test() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Test")
            }

            Spacer(Modifier.width(12.dp))

            OutlinedButton(
                onClick = { vm.clear() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }

        Spacer(Modifier.height(20.dp))

        if (state.error.isNotEmpty()) {
            Text("Error:", style = MaterialTheme.typography.titleMedium)
            Text(state.error, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
        }

        Text("HTTP Status:", style = MaterialTheme.typography.titleMedium)
        Text(state.status)
        Spacer(Modifier.height(16.dp))

        Text("Headers:", style = MaterialTheme.typography.titleMedium)
        Text(state.headers)
        Spacer(Modifier.height(16.dp))

        Text("JSON:", style = MaterialTheme.typography.titleMedium)
        Text(state.json)
    }
}
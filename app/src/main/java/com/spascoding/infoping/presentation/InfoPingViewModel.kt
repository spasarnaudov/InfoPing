package com.spascoding.infoping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spascoding.infoping.data.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoPingViewModel : ViewModel() {

    private val repo = ApiRepository()

    private val _state = MutableStateFlow(InfoPingState(url = "http://192.168.0.6/"))
    val state = _state.asStateFlow()

    fun updateUrl(newUrl: String) {
        _state.value = _state.value.copy(url = newUrl)
    }

    fun clear() {
        _state.value = InfoPingState(url = _state.value.url)
    }

    fun test() {
        val url = _state.value.url

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.testUrl(url)
            _state.value = result
        }
    }
}

data class InfoPingState(
    val url: String = "",
    val status: String = "",
    val headers: String = "",
    val json: String = "",
    val error: String = ""
)
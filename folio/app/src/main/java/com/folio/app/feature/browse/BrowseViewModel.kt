package com.folio.app.feature.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folio.app.core.model.ExtensionEntry
import com.folio.app.core.model.ExtensionIndex
import com.folio.app.core.model.ExtensionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor() : ViewModel() {

    data class UiState(
        val repos: List<ExtensionRepo> = emptyList(),
        val extensions: Map<String, List<ExtensionEntry>> = emptyMap(),
        val loading: Set<String> = emptySet(),
        val errors: Map<String, String> = emptyMap(),
    )

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    fun addRepo(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return
        if (_state.value.repos.any { it.url == trimmed }) return

        val repo = ExtensionRepo(url = trimmed)
        _state.update { it.copy(repos = it.repos + repo, loading = it.loading + trimmed) }

        viewModelScope.launch {
            try {
                val entries = fetchIndex(trimmed)
                _state.update { s ->
                    s.copy(
                        extensions = s.extensions + (trimmed to entries),
                        loading = s.loading - trimmed,
                        errors = s.errors - trimmed,
                    )
                }
            } catch (e: Exception) {
                _state.update { s ->
                    s.copy(
                        loading = s.loading - trimmed,
                        errors = s.errors + (trimmed to (e.message ?: "Unknown error")),
                    )
                }
            }
        }
    }

    fun removeRepo(url: String) {
        _state.update { s ->
            s.copy(
                repos = s.repos.filter { it.url != url },
                extensions = s.extensions - url,
                errors = s.errors - url,
            )
        }
    }

    private suspend fun fetchIndex(url: String): List<ExtensionEntry> = withContext(Dispatchers.IO) {
        val text = java.net.URL(url).openStream().bufferedReader().readText()
        json.decodeFromString<ExtensionIndex>(text).sources
    }
}

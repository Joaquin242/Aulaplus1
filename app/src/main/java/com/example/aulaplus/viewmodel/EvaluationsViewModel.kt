package com.example.aulaplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aulaplus.api.dto.EvaluacionDto
import com.example.aulaplus.repository.EvaluationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EvaluationsUiState(
    val loading: Boolean = true,
    val data: List<EvaluacionDto> = emptyList(),
    val error: String? = null
)

class EvaluationsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = EvaluationsRepository()
    private val _ui = MutableStateFlow(EvaluationsUiState())
    val ui: StateFlow<EvaluationsUiState> = _ui.asStateFlow()

    fun load(email: String) {
        _ui.value = EvaluationsUiState(loading = true)
        viewModelScope.launch {
            runCatching { repo.fetch(email) }
                .onSuccess { _ui.value = EvaluationsUiState(loading = false, data = it) }
                .onFailure { _ui.value = EvaluationsUiState(loading = false, error = it.message) }
        }
    }
}

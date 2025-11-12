package com.example.aulaplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aulaplus.api.dto.CursoDto
import com.example.aulaplus.repository.CoursesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CoursesUiState(
    val loading: Boolean = true,
    val data: List<CursoDto> = emptyList(),
    val error: String? = null
)

class CoursesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CoursesRepository()
    private val _ui = MutableStateFlow(CoursesUiState())
    val ui: StateFlow<CoursesUiState> = _ui.asStateFlow()

    fun load(email: String) {
        _ui.value = CoursesUiState(loading = true)
        viewModelScope.launch {
            runCatching { repo.fetch(email) }
                .onSuccess { _ui.value = CoursesUiState(loading = false, data = it) }
                .onFailure { _ui.value = CoursesUiState(loading = false, error = it.message) }
        }
    }
}

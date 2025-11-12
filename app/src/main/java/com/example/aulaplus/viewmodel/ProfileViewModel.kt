package com.example.aulaplus.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aulaplus.repository.AvatarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class User(val name: String, val email: String)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = User("Joaquín Tapia", "joaquin@aulaplus.cl"),
    val error: String? = null,
    val avatarUri: Uri? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val avatarRepository = AvatarRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // Cargar avatar guardado al iniciar
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { saved ->
                _uiState.update { it.copy(avatarUri = saved) }
            }
        }
    }

    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch { avatarRepository.saveAvatarUri(uri) }
    }
}

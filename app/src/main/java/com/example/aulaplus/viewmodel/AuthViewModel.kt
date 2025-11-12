package com.example.aulaplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aulaplus.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val currentEmail: String? = null,
    val usersCount: Int = 0,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(application)

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthRepository.User?>(null)
    val currentUser: StateFlow<AuthRepository.User?> = _currentUser.asStateFlow()

    private var users: MutableList<AuthRepository.User> = mutableListOf()

    init {
        viewModelScope.launch {
            combine(repo.getUsers(), repo.getCurrentEmail()) { list, email ->
                users = list.toMutableList()
                val u = if (email.isNullOrBlank()) null else list.find { it.email == email }
                _currentUser.value = u
                _ui.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = u != null,
                        currentEmail = email,
                        usersCount = list.size,
                        error = null
                    )
                }
            }.collect()
        }
    }

    fun register(name: String, email: String, pass: String, confirm: String) {
        val trimmedEmail = email.trim().lowercase()

        val msg = when {
            name.isBlank() -> "Ingresa tu nombre."
            trimmedEmail.isBlank() -> "Ingresa tu correo."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Correo inválido."
            pass.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
            pass != confirm -> "Las contraseñas no coinciden."
            users.any { it.email == trimmedEmail } -> "Ya existe una cuenta con ese correo."
            else -> null
        }
        if (msg != null) {
            _ui.update { it.copy(error = msg) }
            return
        }

        viewModelScope.launch {
            users.add(AuthRepository.User(name.trim(), trimmedEmail, pass))
            repo.saveUsers(users)
            repo.setCurrentEmail(trimmedEmail)
            _ui.update { it.copy(isLoggedIn = true, currentEmail = trimmedEmail, error = null) }
        }
    }

    fun login(email: String, pass: String) {
        val trimmedEmail = email.trim().lowercase()
        val user = users.find { it.email == trimmedEmail && it.password == pass }
        if (user == null) {
            _ui.update { it.copy(error = "Correo o contraseña incorrectos.") }
            return
        }
        viewModelScope.launch {
            repo.setCurrentEmail(trimmedEmail)
            _ui.update { it.copy(isLoggedIn = true, currentEmail = trimmedEmail, error = null) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.setCurrentEmail(null)
            _ui.update { it.copy(isLoggedIn = false, currentEmail = null) }
            _currentUser.value = null
        }
    }
}

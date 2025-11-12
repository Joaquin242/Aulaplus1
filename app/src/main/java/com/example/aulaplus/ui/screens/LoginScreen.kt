package com.example.aulaplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    error: String?,
    onLogin: (email: String, pass: String) -> Unit,
    onGoRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = pass, onValueChange = { pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { onLogin(email, pass) }, modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }

        TextButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

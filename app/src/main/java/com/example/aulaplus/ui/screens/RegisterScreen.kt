package com.example.aulaplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    error: String?,
    onRegister: (name: String, email: String, pass: String, confirm: String) -> Unit,
    onGoLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = pass, onValueChange = { pass = it },
            label = { Text("Contraseña (mín 6)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = confirm, onValueChange = { confirm = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onRegister(name, email, pass, confirm) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarme")
        }

        TextButton(onClick = onGoLogin, modifier = Modifier.fillMaxWidth()) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

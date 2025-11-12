package com.example.aulaplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenProfile: () -> Unit,
    onOpenCourses: () -> Unit,
    onOpenEvaluaciones: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Bienvenido a Aulaplus!", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))

        Button(onClick = onOpenProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Perfil")
        }
        Spacer(Modifier.height(12.dp))

        Button(onClick = onOpenCourses, modifier = Modifier.fillMaxWidth()) {
            Text("Mis Cursos")
        }
        Spacer(Modifier.height(12.dp))

        Button(onClick = onOpenEvaluaciones, modifier = Modifier.fillMaxWidth()) {
            Text("Mis Evaluaciones")
        }
    }
}

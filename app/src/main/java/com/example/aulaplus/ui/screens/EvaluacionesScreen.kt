package com.example.aulaplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aulaplus.ui.components.SimpleTable
import com.example.aulaplus.viewmodel.AuthViewModel
import com.example.aulaplus.viewmodel.EvaluationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluacionesScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    val authVm: AuthViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val auth = authVm.ui.collectAsState()

    val vm: EvaluationsViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val ui = vm.ui.collectAsState()

    LaunchedEffect(auth.value.currentEmail) {
        auth.value.currentEmail?.let { vm.load(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Evaluaciones") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                ui.value.loading -> CircularProgressIndicator()
                ui.value.error != null -> Text(
                    "Error: ${ui.value.error}",
                    color = MaterialTheme.colorScheme.error
                )
                else -> {
                    SimpleTable(
                        headers = listOf("Evaluación", "Curso", "Estado"),
                        rows = ui.value.data.map { listOf(it.evaluacion, it.curso, it.estado) },
                        columnWeights = listOf(1.3f, 1.0f, 1.0f)
                    )
                }
            }
        }
    }
}

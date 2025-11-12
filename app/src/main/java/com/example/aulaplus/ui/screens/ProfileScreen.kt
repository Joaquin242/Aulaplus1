package com.example.aulaplus.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.aulaplus.ui.components.ImagePickerDialog
import com.example.aulaplus.viewmodel.ProfileUiState
import com.example.aulaplus.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    name: String,
    email: String,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val vm: ProfileViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )
    val ui by vm.uiState.collectAsState()

    var showPicker by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.CAMERA)
    } else {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(permissions)

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> if (success && tempCameraUri != null) vm.updateAvatar(tempCameraUri) }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> uri?.let { vm.updateAvatar(it) } }

    val getContentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { vm.updateAvatar(it) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } },
                actions = {
                    TextButton(onClick = onLogout) { Text("Cerrar sesión") }
                }
            )
        }
    ) { inner ->
        // diálogo de selección
        if (showPicker) {
            ImagePickerDialog(
                onDismiss = { showPicker = false },
                onCameraClick = {
                    showPicker = false
                    val hasCamera = permissionsState.permissions.any {
                        it.permission == Manifest.permission.CAMERA && it.status.isGranted
                    }
                    if (!hasCamera) {
                        permissionsState.launchMultiplePermissionRequest()
                    } else {
                        tempCameraUri = createImageUri(context)
                        tempCameraUri?.let { takePictureLauncher.launch(it) }
                    }
                },
                onGalleryClick = {
                    showPicker = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        val hasRead = permissionsState.permissions.any {
                            it.permission == Manifest.permission.READ_EXTERNAL_STORAGE && it.status.isGranted
                        }
                        if (!hasRead) {
                            permissionsState.launchMultiplePermissionRequest()
                        } else {
                            getContentLauncher.launch("image/*")
                        }
                    }
                }
            )
        }

        ProfileContent(
            modifier = Modifier.padding(inner).padding(24.dp),
            uiState = ui,
            displayName = name,
            displayEmail = email,
            onPick = { showPicker = true }
        )
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    displayName: String,
    displayEmail: String,
    onPick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(120.dp), contentAlignment = Alignment.BottomEnd) {
                    if (uiState.avatarUri != null) {
                        AsyncImage(
                            model = uiState.avatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { onPick() },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { onPick() },
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.Person, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(56.dp)
                                )
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier.size(36.dp).clickable { onPick() },
                        shape = CircleShape,
                        tonalElevation = 4.dp
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = "Cambiar foto")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(displayName, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text(displayEmail, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFile = File(
        context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
        "profile_avatar_$timeStamp.jpg"
    )
    return try {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    } catch (_: Exception) { null }
}

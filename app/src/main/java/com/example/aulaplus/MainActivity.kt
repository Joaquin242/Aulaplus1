package com.example.aulaplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aulaplus.ui.screens.CoursesScreen
import com.example.aulaplus.ui.screens.EvaluacionesScreen
import com.example.aulaplus.ui.screens.HomeScreen
import com.example.aulaplus.ui.screens.LoginScreen
import com.example.aulaplus.ui.screens.ProfileScreen
import com.example.aulaplus.ui.screens.RegisterScreen
import com.example.aulaplus.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {

                val nav: NavHostController = rememberNavController()

                // ViewModel de autenticación (DataStore)
                val authVm: AuthViewModel = viewModel(
                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                )
                val authUi = authVm.ui.collectAsState()
                val currentUser = authVm.currentUser.collectAsState()

                // Si hay sesión guardada → Home; si no → Login
                val start = if (authUi.value.isLoggedIn) "home" else "login"

                NavHost(navController = nav, startDestination = start) {

                    composable("login") {
                        LoginScreen(
                            error = authUi.value.error,
                            onLogin = { email, pass -> authVm.login(email, pass) },
                            onGoRegister = { nav.navigate("register") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            error = authUi.value.error,
                            onRegister = { name, email, pass, confirm ->
                                authVm.register(name, email, pass, confirm)
                            },
                            onGoLogin = {
                                nav.popBackStack()
                                nav.navigate("login")
                            }
                        )
                    }

                    composable("home") {
                        // Si no hay sesión por cualquier motivo, ir a login
                        if (!authUi.value.isLoggedIn) {
                            nav.popBackStack(); nav.navigate("login")
                        }
                        HomeScreen(
                            onOpenProfile = { nav.navigate("profile") },
                            onOpenCourses = { nav.navigate("courses") },
                            onOpenEvaluaciones = { nav.navigate("evaluaciones") }
                        )
                    }

                    composable("courses") {
                        CoursesScreen(
                            onBack = {
                                nav.popBackStack()
                                nav.navigate("home")
                            }
                        )
                    }

                    composable("evaluaciones") {
                        EvaluacionesScreen(
                            onBack = {
                                nav.popBackStack()
                                nav.navigate("home")
                            }
                        )
                    }

                    composable("profile") {
                        // Seguridad: si no hay sesión, volver a login
                        if (!authUi.value.isLoggedIn) {
                            nav.popBackStack(); nav.navigate("login")
                        }
                        ProfileScreen(
                            name = currentUser.value?.name ?: "Usuario",
                            email = currentUser.value?.email ?: "",
                            onBack = {
                                nav.popBackStack()
                                nav.navigate("home")
                            },
                            onLogout = {
                                authVm.logout()
                                nav.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }

                // Reaccionar a login/registro y saltar a Home si estás en auth
                val currentRoute = nav.currentDestination?.route
                if (authUi.value.isLoggedIn && currentRoute in listOf("login", "register")) {
                    nav.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
    }
}

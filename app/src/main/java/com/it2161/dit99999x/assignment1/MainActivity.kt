package com.it2161.dit99999x.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.it2161.dit99999x.assignment1.ui.components.LoginScreen
import com.it2161.dit99999x.assignment1.ui.components.LandingScreen
import com.it2161.dit99999x.assignment1.ui.components.RegisterUserScreen
import com.it2161.dit99999x.assignment1.ui.components.MovieDetailScreen
import com.it2161.dit99999x.assignment1.ui.components.CommentMovieScreen
import com.it2161.dit99999x.assignment1.ui.components.ProfileScreen
import com.it2161.dit99999x.assignment1.ui.theme.Assignment1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment1Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        // Add other composable destinations here
                        composable("landing") { LandingScreen(navController) }
                        composable("register") { RegisterUserScreen(navController) }
                        composable("movieDetail") { MovieDetailScreen() }
                        composable("comment") { CommentMovieScreen() }
                        composable("profile") { ProfileScreen(navController) }
                        // ... (Your other composable routes)
                    }
                }
            }
        }
    }
}
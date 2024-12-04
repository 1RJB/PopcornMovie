package com.it2161.dit233774U.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.it2161.dit233774U.assignment1.ui.components.LoginScreen
import com.it2161.dit233774U.assignment1.ui.components.LandingScreen
import com.it2161.dit233774U.assignment1.ui.components.RegisterUserScreen
import com.it2161.dit233774U.assignment1.ui.components.MovieDetailScreen
import com.it2161.dit233774U.assignment1.ui.components.AddCommentScreen
import com.it2161.dit233774U.assignment1.ui.components.ProfileScreen
import com.it2161.dit233774U.assignment1.ui.theme.Assignment1Theme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.it2161.dit233774U.assignment1.data.Comments
import com.it2161.dit233774U.assignment1.data.MovieItem
import com.it2161.dit233774U.assignment1.ui.components.ViewCommentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment1Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val gson = Gson()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("landing") { LandingScreen(navController) }
                        composable("register") { RegisterUserScreen(navController) }
                        composable(
                            "movieDetail/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val movie = gson.fromJson(movieJson, MovieItem::class.java)
                            MovieDetailScreen(navController, movie) // Call MovieDetailScreen with movie data
                        }
                        composable(
                            "comment/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val movie = gson.fromJson(movieJson, MovieItem::class.java)
                            AddCommentScreen(navController, movie) // Call AddCommentScreen with movie data
                        }
                        composable(
                            "view_comment/{movieTitle}/{commentJson}",
                            arguments = listOf(
                                navArgument("movieTitle") { type = NavType.StringType },
                                navArgument("commentJson") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: "Movie"
                            val commentJson = backStackEntry.arguments?.getString("commentJson")
                            val comment = gson.fromJson(commentJson, Comments::class.java)
                            ViewCommentScreen(navController, movieTitle, comment) // Call ViewCommentScreen with comment data and movieTitle
                        }
                        composable(
                            "add_comment/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val movie = gson.fromJson(movieJson, MovieItem::class.java)
                            AddCommentScreen(navController, movie)
                        }
                        composable("profile") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}
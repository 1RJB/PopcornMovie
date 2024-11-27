package com.it2161.dit99999x.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.it2161.dit99999x.assignment1.data.Comments
import com.it2161.dit99999x.assignment1.data.MovieItem
import com.it2161.dit99999x.assignment1.ui.components.formatDateTime
import com.it2161.dit99999x.assignment1.ui.components.getInitials

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
                        composable("landing") { LandingScreen(navController) }
                        composable("register") { RegisterUserScreen(navController) }
                        composable(
                            "movieDetail/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val gson = Gson()
                            val movie = gson.fromJson(movieJson, MovieItem::class.java)
                            MovieDetailScreen(navController, movie) // Call MovieDetailScreen with movie data
                        }

                        composable(
                            "comment/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val gson = Gson()
                            val movie = gson.fromJson(movieJson, MovieItem::class.java)
                            CommentMovieScreen(navController, movie) // Call CommentScreen with movie data
                        }
                        composable(
                            "view_comments/{commentJson}",
                            arguments = listOf(navArgument("commentJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val commentJson = backStackEntry.arguments?.getString("commentJson")
                            val gson = Gson()
                            val comment = gson.fromJson(commentJson, Comments::class.java)

                            // Apply padding to the content
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .padding(
                                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                                        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                                    )
                            ) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    // User initials in a circle
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.LightGray, shape = CircleShape)
                                            .wrapContentSize(Alignment.Center)
                                    ) {
                                        Text(
                                            getInitials(comment.user),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(comment.user, style = MaterialTheme.typography.bodyMedium)
                                        Text(formatDateTime(comment.date, comment.time), style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(comment.comment, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
                            }
                        }

                        composable(
                            "add_comment/{movieJson}",
                            arguments = listOf(navArgument("movieJson") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieJson = backStackEntry.arguments?.getString("movieJson")
                            val gson = Gson()
                            val movie = gson.fromJson(movieJson, MovieItem::class.java) // Convert JSON back to movie object
                            CommentMovieScreen(navController, movie) // Call CommentMovieScreen with movie data
                        }
                        composable("profile") { ProfileScreen(navController) }
                    }
                }
            }
        }
    }
}
package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.MovieItem
import com.it2161.dit99999x.assignment1.data.Comments
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(navController: NavController, movie: MovieItem) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(movie.title, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("landing") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                       DropdownMenuItem(
                            text = { Text("Add Comments") },
                            onClick = {
                                val gson = Gson()
                                val movieJson = gson.toJson(movie) // Convert movie object to JSON
                                navController.navigate("add_comment/$movieJson") // Pass movie JSON as argument
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = MovieRaterApplication.instance.getImgVector(movie.image).asImageBitmap(),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display movie details from JSON
            MovieDetails(movie)

            Spacer(modifier = Modifier.height(16.dp))

            // Display comments
            CommentsSection(movie.comment, navController)
        }
    }
}

@Composable
fun MovieDetails(movie: MovieItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium)
        Text("Synopsis: ${movie.synopsis}", style = MaterialTheme.typography.bodyMedium)
        Text("Cast: ${movie.actors.joinToString()}", style = MaterialTheme.typography.bodyMedium)
        Text("Director: ${movie.director}", style = MaterialTheme.typography.bodyMedium)
        Text("Genre: ${movie.genre}", style = MaterialTheme.typography.bodyMedium)
        Text("Length: ${movie.length} minutes", style = MaterialTheme.typography.bodyMedium)
        // ... add other details as needed
    }
}

@Composable
fun CommentsSection(comments: List<Comments>, navController: NavController) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(comments.sortedByDescending { it.date + " " + it.time }) { comment -> // Sort by combined date and time
            CommentItem(comment, navController)
        }
    }
}

@Composable
fun CommentItem(comment: Comments, navController: NavController) {
    val gson = Gson()
    Column(modifier = Modifier
        .clickable { navController.navigate("view_comments/${gson.toJson(comment)}") }
        .padding(8.dp)) {
        Text(comment.user, style = MaterialTheme.typography.bodyMedium)
        Text(comment.comment, style = MaterialTheme.typography.bodySmall)
        Text(formatDateTime(comment.date, comment.time), style = MaterialTheme.typography.bodySmall)
    }
}

fun formatDateTime(date: String, time: String): String {
    val combinedDateTime = "$date $time" // Combine date and time strings
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // Adjust format if needed
    val commentDateTime = formatter.parse(combinedDateTime)

    val currentDateTime = Calendar.getInstance()

    val diff = currentDateTime.timeInMillis - commentDateTime.time
    val hoursAgo = diff / (1000 * 60 * 60)
    val daysAgo = diff / (1000 * 60 * 60 * 24)

    return when {
        hoursAgo < 24 -> "$hoursAgo hrs ago"
        daysAgo == 1L -> "1 day ago"
        else -> "$daysAgo days ago"
    }
}
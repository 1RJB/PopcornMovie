package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.MovieItem
import com.it2161.dit99999x.assignment1.data.Comments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(navController: NavController, movie: MovieItem) {
    var showMenu by remember { mutableStateOf(false) }

    // Find the movie in MovieRaterApplication's data
    val currentMovie = remember {
        mutableStateOf(MovieRaterApplication.instance.data.find { it.title == movie.title } ?: movie)
    }

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
                                navController.navigate("add_comment/$movieJson")
                                showMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    bitmap = MovieRaterApplication.instance.getImgVector(movie.image)
                        .asImageBitmap(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .aspectRatio(0.95f) // Make it square
                        .height(140.dp) // Maintain desired height
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Display movie details from JSON
                MovieDetails(currentMovie.value)

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Display comments without LazyColumn
                CommentsSection(currentMovie.value.comment, movie, navController)
            }
        }
    }
}

@Composable
fun MovieDetails(movie: MovieItem) {
    Column(modifier = Modifier.padding(16.dp)) {
            Text("Director: ${movie.director}", style = MaterialTheme.typography.bodyMedium)
            Text("Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium)
            Text("Rating: ${movie.ratings_score} / 10", style = MaterialTheme.typography.bodyMedium)
            Text("Actors: ${movie.actors.joinToString()}", style = MaterialTheme.typography.bodyMedium)
            Text("Genre: ${movie.genre}", style = MaterialTheme.typography.bodyMedium)
            Text("Length: ${movie.length} minutes", style = MaterialTheme.typography.bodyMedium)
            Text("Synopsis: ${movie.synopsis}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CommentsSection(comments: List<Comments>, movie: MovieItem, navController: NavController) {
    Text("Comments", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.padding(top=16.dp))
    Column(modifier = Modifier.padding(16.dp)) {
            for (comment in comments.sortedByDescending { it.date + " " + it.time }) {
                CommentItem(comment, movie, navController)
            }
    }
}


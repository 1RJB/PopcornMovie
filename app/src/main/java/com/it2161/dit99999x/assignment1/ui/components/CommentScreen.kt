package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.Comments
import com.it2161.dit99999x.assignment1.data.MovieItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentMovieScreen(navController: NavController, movie: MovieItem) {
    var commentText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    // Find the movie in MovieRaterApplication's data
    var currentMovie by remember {
        mutableStateOf(
            MovieRaterApplication.instance.data.find { it.title == movie.title } ?: movie
        )
    }

    Scaffold(
        topBar = { MovieRaterTopAppBar(currentMovie.title, navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Comment Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = MovieRaterApplication.instance.getImgVector(movie.image).asImageBitmap(),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .aspectRatio(0.95f)
                        .height(140.dp)
                        .padding(16.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Add comment") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val newComment = Comments(
                        user = MovieRaterApplication.instance.userProfile!!.userName,
                        comment = commentText,
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                        time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    )
                    // Update movie comments in MovieRaterApplication's data
                    val updatedData = MovieRaterApplication.instance.data.map { existingMovie ->
                        if (existingMovie.title == currentMovie.title) {
                            existingMovie.copy(comment = existingMovie.comment.toMutableList().also { it.add(0, newComment) })
                        } else {
                            existingMovie
                        }
                    }.toMutableList()
                    MovieRaterApplication.instance.data = updatedData

                    // Update currentMovie to trigger recomposition
                    currentMovie = MovieRaterApplication.instance.data.find { it.title == currentMovie.title }!!

                    commentText = "" // Clear the comment text field

                    // Navigate to the movie details page
                    val gson = Gson()
                    val movieJson = gson.toJson(currentMovie)
                    navController.navigate("movieDetail/$movieJson") // Navigate to movie details
                }) {
                    Text("Submit")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View Comments Section
            Column {
                for (comment in currentMovie.comment.sortedByDescending { it.date + " " + it.time }) {
                    CommentItem(comment, movie, navController) // Use imported CommentItem
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comments, movie: MovieItem, navController: NavController) {
    val gson = Gson()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("view_comments/${movie.title}/${gson.toJson(comment)}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(comment.user, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        formatDateTime(comment.date, comment.time),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(comment.comment, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ViewCommentScreen(navController: NavController, movieTitle: String, comment: Comments) {
    Scaffold(
        topBar = { MovieRaterTopAppBar(movieTitle, navController) } // Set movieTitle as the top bar title
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
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
                        Text(
                            formatDateTime(comment.date, comment.time),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    comment.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieRaterTopAppBar(title: String, navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text(title, textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

// Helper function to get user initials
fun getInitials(username: String): String {
    val parts = username.split(Regex("(?<=[a-z])(?=[A-Z])|_|\\s+")) // Split by camel case, underscore, or whitespace
    return if (parts.size >= 2) {
        parts[0][0] + "." + parts[1][0] + "."
    } else if (parts.isNotEmpty()) {
        parts[0][0] + "."
    } else {
        ""
    }
}

// Helper function to format date and time
fun formatDateTime(date: String, time: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val commentDateTime = LocalDateTime.parse("$date $time", formatter)
        .atZone(ZoneId.systemDefault()) // Assuming the comment's timezone is the system's default
        .toInstant() // No need to specify ZoneOffset here

    val currentDateTime = LocalDateTime.now().toInstant(ZoneOffset.UTC) // Specify ZoneOffset for current time

    val secondsAgo = ChronoUnit.SECONDS.between(commentDateTime, currentDateTime)
    val minutesAgo = ChronoUnit.MINUTES.between(commentDateTime, currentDateTime)
    val hoursAgo = ChronoUnit.HOURS.between(commentDateTime, currentDateTime)
    val daysAgo = ChronoUnit.DAYS.between(commentDateTime, currentDateTime)

    return when {
        secondsAgo < 60 -> "$secondsAgo secs ago"
        minutesAgo < 60 -> "$minutesAgo mins ago"
        hoursAgo < 24 -> "$hoursAgo hrs ago"
        daysAgo == 1L -> "1 day ago"
        else -> "$daysAgo days ago"
    }
}
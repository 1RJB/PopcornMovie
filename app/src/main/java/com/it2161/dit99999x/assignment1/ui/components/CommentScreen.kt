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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentMovieScreen(navController: NavController, movie: MovieItem) {
    var commentText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { MovieRaterTopAppBar(movie.title, navController) }
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
                        user = MovieRaterApplication.instance.userProfile!!.userName, // Replace with actual username
                        comment = commentText,
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                        time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    )
                    movie.comment = movie.comment.toMutableList().also { it.add(0, newComment) } // Add to the top of the list
                    MovieRaterApplication.instance.data = MovieRaterApplication.instance.data.map {
                        if (it.title == movie.title) {
                            it.copy(comment = movie.comment)
                        } else {
                            it
                        }
                    }.toMutableList()
                    commentText = "" // Clear the comment text field
                }) {
                    Text("Submit")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View Comments Section
            Column {
                for (comment in movie.comment.sortedByDescending { it.date + " " + it.time }) {
                    CommentItem(comment, navController) // Use imported CommentItem
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comments, navController: NavController) {
    val gson = Gson()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("view_comments/${gson.toJson(comment)}") },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
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
fun ViewCommentScreen(navController: NavController, comment: Comments) {
    Scaffold(
        topBar = { MovieRaterTopAppBar("Comment Details", navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Apply inner padding from Scaffold
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
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
    val combinedDateTime = "$date $time"
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val commentDateTime = formatter.parse(combinedDateTime)

    val currentDateTime = Calendar.getInstance()

    val diff = currentDateTime.timeInMillis - commentDateTime.time
    val secondsAgo = diff / 1000
    val minutesAgo = diff / (1000 * 60)
    val hoursAgo = diff / (1000 * 60 * 60)
    val daysAgo = diff / (1000 * 60 * 60 * 24)

    return when {
        secondsAgo < 60 -> "$secondsAgo secs ago"
        minutesAgo < 60 -> "$minutesAgo mins ago"
        hoursAgo < 24 -> "$hoursAgo hrs ago"
        daysAgo == 1L -> "1 day ago"
        else -> "$daysAgo days ago"
    }
}
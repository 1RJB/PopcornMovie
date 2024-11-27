package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.Comments
import com.it2161.dit99999x.assignment1.data.MovieItem
import java.text.SimpleDateFormat
import java.util.*
import com.it2161.dit99999x.assignment1.ui.components.CommentItem // Import CommentItem from MovieDetail.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentMovieScreen(navController: NavController, movie: MovieItem) {
    var commentText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(movie.title, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
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
                        .aspectRatio(0.95f) // Make it square
                        .height(140.dp) // Maintain desired height
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
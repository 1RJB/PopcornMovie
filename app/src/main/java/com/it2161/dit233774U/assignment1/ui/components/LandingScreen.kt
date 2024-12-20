package com.it2161.dit233774U.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.it2161.dit233774U.assignment1.MovieRaterApplication
import com.it2161.dit233774U.assignment1.data.MovieItem
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController) {

    val gson = Gson()

    // Load movie data
    val movieList = remember {
        MovieRaterApplication.instance.data.toList()
    }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PopCornMovie") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                navController.navigate("profile")
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                navController.navigate("login")
                                showMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(movieList) { movie ->
                MovieItemCard(movie) {
                    navController.navigate("movieDetail/${gson.toJson(movie)}")
                }
            }
        }
    }
}

@Composable
fun MovieItemCard(movie: MovieItem, onMovieClick: () -> Unit) {
    var imageHeight by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                bitmap = MovieRaterApplication.instance.getImgVector(movie.image).asImageBitmap(),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .height(200.dp)
                    .onGloballyPositioned { coordinates ->
                        imageHeight = coordinates.size.height
                    }
            )
            Column(modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
            ) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Max no. of chars based image height and font size
                    val maxChars = (imageHeight / (MaterialTheme.typography.bodyMedium.fontSize.value) * 6).toInt()
                    val truncatedSynopsis = if (movie.synopsis.length > maxChars) {
                        movie.synopsis.substring(0, maxChars) + "..."
                    } else {
                        movie.synopsis
                    }
                    Text(truncatedSynopsis, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text((movie.ratings_score).toString(),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(navController = rememberNavController())
}
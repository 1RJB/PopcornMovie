package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.MovieItem
import com.it2161.dit99999x.assignment1.R // Assuming you have the poster image in your resources
import com.it2161.dit99999x.assignment1.data.Comments
import org.json.JSONArray
import org.json.JSONObject
import com.google.gson.Gson
import jsonData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController) {
    val context = LocalContext.current

    // Parse JSON data
    val movieList = remember {
        val jsonArray = JSONArray(jsonData)
        val gson = Gson()
        val movies = mutableListOf<MovieItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val director = jsonObject.getString("director")
            val releaseDate = jsonObject.getString("release_date")
            val rating = jsonObject.getString("rating").split("/")[0].toFloatOrNull() ?: 0f
            val actors = gson.fromJson(jsonObject.getJSONArray("actors").toString(), Array<String>::class.java).toList()
            val image = jsonObject.getString("image")
            val genre = jsonObject.getString("genre")
            val length = jsonObject.getInt("length")
            val synopsis = jsonObject.getString("synopsis")
            val comments = gson.fromJson(jsonObject.getJSONArray("comments").toString(), Array<Comments>::class.java).toList()

            val movieItem = MovieItem(title, director, releaseDate, rating, actors, image, genre, length, synopsis, comments)
            movies.add(movieItem)
        }
        movies
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PopCornMovie",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "View Profile")
                    }
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
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
                    // Navigate to Movie Detail screen, passing movie ID or object
                    navController.navigate("movieDetails/${movie.title}")
                }
            }
        }
    }
}

@Composable
fun MovieItemCard(movie: MovieItem, onMovieClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Replace with your actual image loading logic
            // Using painterResource as a placeholder
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your image resource
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(movie.synopsis, style = MaterialTheme.typography.bodyMedium)
                // Add ratings display here
                Row() {
                    // Implement star icons or rating text here
                    Text((movie.ratings_score).toString()) // Temporary display of rating
                }
            }
        }
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    //LandingScreen()
}
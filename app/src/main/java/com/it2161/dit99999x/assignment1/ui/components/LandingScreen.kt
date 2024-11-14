package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.it2161.dit99999x.assignment1.MovieRaterApplication

@Composable
fun LandingScreen() {


    val context = LocalContext.current
    var movieList = MovieRaterApplication().data

}

@Preview
@Composable
fun LandingScreenPreview() {

    LandingScreen()
}





package com.it2161.dit99999x.assignment1.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.it2161.dit99999x.assignment1.R
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.navigation.compose.rememberNavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.movie_viewer_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(430.dp)
                .padding(top = 80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 10.dp))

        Spacer(modifier = Modifier.height(25.dp)) // Add spacing between logo and controls

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        )

        Spacer(modifier = Modifier.height(45.dp))

        Button(
            onClick = {
                // Handle login and navigate to Landing screen if successful
                if (isValidLogin(username, password)) {
                    navController.navigate("landing")
                } else {
                    // Show error message (e.g., using a Toast)
                    Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// Placeholder for login validation logic
fun isValidLogin(username: String, password: String): Boolean {
    val userProfile = MovieRaterApplication.instance.userProfile
    if (userProfile!!.userName.isNotEmpty()) {
        return userProfile.userName == username && userProfile.password == password
    }
    return username == "TestUser1" && password == "TestPassword1"

}

@Preview
@Composable
fun LoginUIPreview() {
    LoginScreen(navController = rememberNavController()) // Pass a NavController for preview
}
package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.R
import com.it2161.dit99999x.assignment1.data.UserProfile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    val userProfile = remember { mutableStateOf(MovieRaterApplication.instance.userProfile?.copy()
        ?: UserProfile("TestUser1", "TestPassword1","testuser1@example.com", "Prefer not to say", "98765432", false, "10/01/1970")) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imageHeight = screenHeight / 3

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isEditing) {
                CenterAlignedTopAppBar(
                    title = { Text("Edit Profile") },
                    navigationIcon = {
                        IconButton(onClick = {
                            isEditing = false
                            navController.navigate("profile")
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            MovieRaterApplication.instance.userProfile = userProfile.value
                            isEditing = false
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.Save, contentDescription = "Save")
                        }
                    }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text("Profile") },
                    navigationIcon = {
                        IconButton(onClick = {
                            isEditing = false
                            navController.navigate("landing")
                        }) {
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
                                text = { Text("Edit Profile") },
                                onClick = {
                                    isEditing = true
                                    showMenu = false
                                }
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEditing) {
                EditProfileContent(userProfile)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.avatar_3),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Username: ${userProfile.value.userName}")
                    Text("Email: ${userProfile.value.email}")
                    Text("Gender: ${userProfile.value.gender}")
                    Text("Mobile: ${userProfile.value.mobile}")
                    Text("Year of Birth: ${userProfile.value.yob}")
                }
            }
        }
    }
}

@Composable
fun EditProfileContent(userProfile: MutableState<UserProfile>) {
    // Avatar Selection (similar to Registration screen)
    // ...

    // Input fields for Name, Email, Gender, Mobile, Year of Birth
    // Use OutlinedTextField and remember the values in userProfile state
    // ... Example for Name:
    OutlinedTextField(
        value = userProfile.value.userName,
        onValueChange = { userProfile.value = userProfile.value.copy(userName = it) },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    OutlinedTextField(
        value = userProfile.value.email,
        onValueChange = { userProfile.value = userProfile.value.copy(email = it) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    OutlinedTextField(
        value = userProfile.value.gender,
        onValueChange = { userProfile.value = userProfile.value.copy(gender = it) },
        label = { Text("Gender") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    OutlinedTextField(
        value = userProfile.value.mobile,
        onValueChange = { userProfile.value = userProfile.value.copy(mobile = it) },
        label = { Text("Mobile Number") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    OutlinedTextField(
        value = userProfile.value.yob,
        onValueChange = { userProfile.value = userProfile.value.copy(yob = it) },
        label = { Text("Year of Birth") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )

    // ... Similar OutlinedTextFields for other details ...
}

@Preview
@Composable
fun ProfileScreenPreview() {
    // ProfileScreen() // You might need to provide a NavController for the preview
}
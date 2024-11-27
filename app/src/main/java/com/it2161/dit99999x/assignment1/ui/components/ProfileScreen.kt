package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    var passwordError by remember { mutableStateOf(false) } // Define passwordError state

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
                            navController.navigate("profile")
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
                EditProfileContent(userProfile) { isPasswordError ->
                    passwordError = isPasswordError // Update passwordError state
                }
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
                    modifier = Modifier.fillMaxWidth().padding(30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileDetailRow("Username", userProfile.value.userName)
                    ProfileDetailRow("Year of Birth", userProfile.value.yob)
                    ProfileDetailRow("Gender", userProfile.value.gender)
                    ProfileDetailRow("Mobile", userProfile.value.mobile)
                    ProfileDetailRow("Email", userProfile.value.email)
                    ProfileDetailRow("Receive Updates", if (userProfile.value.updates) "Yes" else "No")
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Add vertical spacing
        horizontalArrangement = Arrangement.SpaceBetween // Space between label and value
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun EditProfileContent(userProfile: MutableState<UserProfile>, onPasswordError: (Boolean) -> Unit) {
    var confirmPassword by remember { mutableStateOf("") } // State for confirm password
    var passwordError by remember { mutableStateOf(false) } // State for password error
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showEmptyFieldError by remember { mutableStateOf(false) } // State for empty field error

    // Input fields for Name, Email, Gender, Mobile, Year of Birth
    OutlinedTextField(
        value = userProfile.value.userName,
        onValueChange = { userProfile.value = userProfile.value.copy(userName = it) },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    OutlinedTextField(
        value = userProfile.value.password,
        onValueChange = {
            userProfile.value = userProfile.value.copy(password = it)
            passwordError = it != confirmPassword // Update passwordError state immediately
            onPasswordError(passwordError)
        },
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        isError = passwordError // Show error state if passwords don't match
    )
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = {
            confirmPassword = it
            passwordError = userProfile.value.password != it // Update passwordError state immediately
            onPasswordError(passwordError)
        },
        label = { Text("Confirm Password") },
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
            }
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        isError = passwordError // Show error state if passwords don't match
    )
    OutlinedTextField(
        value = userProfile.value.yob,
        onValueChange = { userProfile.value = userProfile.value.copy(yob = it) },
        label = { Text("Year of Birth") },
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
        value = userProfile.value.email,
        onValueChange = { userProfile.value = userProfile.value.copy(email = it) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = userProfile.value.updates == true,
            onCheckedChange = { userProfile.value = userProfile.value.copy(updates = it) }
        )
        Text(text = "Receive updates via email")
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
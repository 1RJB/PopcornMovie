package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.UserProfile
import com.it2161.dit99999x.assignment1.R

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
    var passwordError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isEditing) {
                val isSaveEnabled = !passwordError // Disable Save if passwords don't match
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
                        IconButton(
                            onClick = {
                                MovieRaterApplication.instance.userProfile = userProfile.value
                                isEditing = false
                                navController.navigate("profile")
                            },
                            enabled = isSaveEnabled // Enable/Disable based on password validity
                        ) {
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
                    passwordError = isPasswordError
                }
            } else {
                Image(
                    painter = painterResource(id = userProfile.value.profilePicture),
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
                    ProfileDetailRow("To receive updates", if (userProfile.value.updates) "Yes" else "No")
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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
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
    // Validation states for fields
    var userNameError by remember { mutableStateOf("") }
    var mobileNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var yearOfBirthError by remember { mutableStateOf("") }

    // Validation patterns for each rule
    val usernamePattern = "^[a-zA-Z0-9]{3,12}$".toRegex() // Alphanumeric, 3-12 characters
    val mobileNumberPattern = "^\\d{8}$".toRegex() // Exactly 8 digits
    val emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex() // Basic email pattern

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar Selection
        Text("Select Avatar", style = MaterialTheme.typography.titleMedium)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            AvatarOption(R.drawable.avatar_1, userProfile)
            AvatarOption(R.drawable.avatar_2, userProfile)
            AvatarOption(R.drawable.avatar_3, userProfile)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userProfile.value.userName,
            onValueChange = {
                userProfile.value = userProfile.value.copy(userName = it)
                userNameError = when {
                    it.isEmpty() -> "Username is required"
                    !it.matches(usernamePattern) -> "Username must be 3-12 characters, alphanumeric only"
                    else -> ""
                }
            },
            label = { Text(buildAnnotatedString {
                append("Username")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Enter username") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            isError = userNameError.isNotEmpty()
        )
        if (userNameError.isNotEmpty()) {
            Text(userNameError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Update the password fields only when user decides to change the password
        OutlinedTextField(
            value = userProfile.value.password,
            onValueChange = {
                userProfile.value = userProfile.value.copy(password = it)
                passwordError = it != confirmPassword // Check if passwords match
                onPasswordError(passwordError) // Notify parent composable
            },
            label = { Text(buildAnnotatedString {
                append("Password")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Enter password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            isError = passwordError // Display error state if passwords don't match
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordError = userProfile.value.password != it // Check if passwords match
                onPasswordError(passwordError) // Notify parent composable
            },
            label = { Text(buildAnnotatedString {
                append("Confirm Password")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Enter password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            isError = passwordError // Display error state if passwords don't match
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Year of birth using DropdownMenuField
        DropdownMenuField(userProfile.value.yob) { selectedYear ->
            userProfile.value = userProfile.value.copy(yob = selectedYear)
            yearOfBirthError = if (selectedYear.isEmpty()) "Year of birth is required" else ""
        }
        if (yearOfBirthError.isNotEmpty()) {
            Text(yearOfBirthError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gender selection using GenderSelection
        GenderSelection(userProfile.value.gender) { selectedGender ->
            userProfile.value = userProfile.value.copy(gender = selectedGender)
        }

        OutlinedTextField(
            value = userProfile.value.mobile,
            onValueChange = {
                userProfile.value = userProfile.value.copy(mobile = it)
                mobileNumberError = when {
                    it.isEmpty() -> "Mobile number is required"
                    !it.matches(mobileNumberPattern) -> "Mobile number must be 8 digits"
                    else -> ""
                }
            },
            label = { Text(buildAnnotatedString {
                append("Mobile Number")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Enter mobile number") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            isError = mobileNumberError.isNotEmpty()
        )
        if (mobileNumberError.isNotEmpty()) {
            Text(mobileNumberError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userProfile.value.email,
            onValueChange = {
                userProfile.value = userProfile.value.copy(email = it)
                emailError = when {
                    it.isEmpty() -> "Email is required"
                    !it.matches(emailPattern) -> "Invalid email format"
                    else -> ""
                }
            },
            label = { Text(buildAnnotatedString {
                append("Email")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            isError = emailError.isNotEmpty()
        )
        if (emailError.isNotEmpty()) {
            Text(emailError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = userProfile.value.updates,
                onCheckedChange = { userProfile.value = userProfile.value.copy(updates = it) }
            )
            Text(text = "To receive updates")
        }
    }
}

@Composable
fun AvatarOption(avatarResId: Int, userProfile: MutableState<UserProfile>) {
    val isSelected = userProfile.value.profilePicture == avatarResId
    Image(
        painter = painterResource(id = avatarResId),
        contentDescription = "Avatar Option",
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
            .clickable {
                userProfile.value = userProfile.value.copy(profilePicture = avatarResId)
            }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.Black else Color.Transparent
            ),
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
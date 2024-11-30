package com.it2161.dit99999x.assignment1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    var confirmPasswordError by remember { mutableStateOf(false) }
    var userNameError by remember { mutableStateOf("") }
    var mobileNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    // Callback functions to handle errors
    val onUserNameError: (String) -> Unit = { userNameError = it }
    val onEmailError: (String) -> Unit = { emailError = it }
    val onMobileNumberError: (String) -> Unit = { mobileNumberError = it }
    val onPasswordError: (Boolean) -> Unit = { passwordError = it }

    val isSaveEnabled = !passwordError && !confirmPasswordError && userNameError.isEmpty() && mobileNumberError.isEmpty() && emailError.isEmpty() // Disable Save if password has error, confirm password has error, or any other field has error

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
                        IconButton(
                            onClick = {
                                MovieRaterApplication.instance.userProfile = userProfile.value
                                isEditing = false
                                navController.navigate("profile")
                            },
                            enabled = isSaveEnabled // Enable/Disable based on errors
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
                EditProfileContent(
                    userProfile = userProfile,
                    onPasswordError = onPasswordError,
                    onUserNameError = onUserNameError,
                    onMobileNumberError = onMobileNumberError,
                    onEmailError = onEmailError,

                )
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
fun EditProfileContent(
    userProfile: MutableState<UserProfile>,
    onPasswordError: (Boolean) -> Unit,
    onUserNameError: (String) -> Unit,
    onMobileNumberError: (String) -> Unit,
    onEmailError: (String) -> Unit
) {
    // Validation state for fields
    var confirmPasswordError by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordErrorMessage by remember { mutableStateOf("") }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var userNameError by remember { mutableStateOf("") }
    var mobileNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var yearOfBirthError by remember { mutableStateOf("") }

    // Reset the password field to empty only if we're in edit mode and the user hasn't changed the password yet
    val currentPassword = remember { userProfile.value.password }

    // Reset the password field on entering the edit mode to be empty
    val initialPasswordState = remember { mutableStateOf("") }

    // Validation patterns for each rule
    val usernamePattern = "^[a-zA-Z0-9]{3,12}$".toRegex() // Alphanumeric, 3-12 characters
    val mobileNumberPattern = "^\\d{8}$".toRegex() // Exactly 8 digits
    val emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex() // Basic email pattern

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
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
        }

        item {
            OutlinedTextField(
                value = userProfile.value.userName,
                onValueChange = {
                    userProfile.value = userProfile.value.copy(userName = it)
                    userNameError = when {
                        it.isEmpty() -> "Username is required"
                        !it.matches(usernamePattern) -> "Username must be 3-12 characters, alphanumeric only"
                        else -> ""
                    }
                    onUserNameError(userNameError)
                },
                label = {
                    Text(buildAnnotatedString {
                        append("Username")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    })
                },
                placeholder = { Text("Enter username") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                isError = userNameError.isNotEmpty()
            )
            if (userNameError.isNotEmpty()) {
                Text(userNameError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Trigger confirm password validation if password is filled and confirm password is empty
            if (initialPasswordState.value.isNotEmpty() && confirmPassword.isEmpty()) {
                confirmPasswordErrorMessage = "Confirm password is required"
                confirmPasswordError = true
            }
            // Trigger confirm password validation if password is filled and confirm password doesn't match
            else if (initialPasswordState.value.isNotEmpty() && initialPasswordState.value != confirmPassword) {
                confirmPasswordErrorMessage = "Passwords do not match"
                confirmPasswordError = true
            } else {
                confirmPasswordErrorMessage = ""
                confirmPasswordError = false
            }
            confirmPasswordError = confirmPasswordErrorMessage.isNotEmpty()
            onPasswordError(confirmPasswordError) // Trigger callback for password error state

            // Update the password fields only when user decides to change the password
            OutlinedTextField(
                value = initialPasswordState.value, // Start with empty password
                onValueChange = {
                    if (it.isNotEmpty()) {
                        initialPasswordState.value = it // Update only if not empty
                        userProfile.value =
                            userProfile.value.copy(password = it) // Update the userProfile
                        passwordErrorMessage = when {
                            it.length < 8 -> "Password must be at least 8 characters long"
                            !it.any { char -> char.isUpperCase() } -> "Password must contain at least 1 uppercase letter"
                            !it.any { char -> char.isLowerCase() } -> "Password must contain at least 1 lowercase letter"
                            !it.any { char -> char.isDigit() } -> "Password must contain at least 1 number"
                            !it.any { char -> "!@#$%^&*()_+-=<>?/.,;:'\"".contains(char) } -> "Password must contain at least 1 special character"
                            else -> ""
                        }
                    } else {
                        // If password is left empty, reset it to the current password
                        initialPasswordState.value = ""
                        userProfile.value = userProfile.value.copy(password = currentPassword)
                        passwordErrorMessage = ""
                    }
                    onPasswordError(passwordErrorMessage.isNotEmpty())

                    // Trigger confirm password validation on password change
                    if (confirmPassword.isEmpty() && it.isNotEmpty()) {
                        confirmPasswordErrorMessage = "Confirm password is required"
                        confirmPasswordError = true
                    }
                },
                label = { Text("Password") },
                placeholder = { Text("Enter password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                isError = passwordErrorMessage.isNotEmpty()
            )
            if (passwordErrorMessage.isNotEmpty()) {
                Text(passwordErrorMessage, color = MaterialTheme.colorScheme.error)
            }


            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordErrorMessage = when {
                        it != userProfile.value.password -> "Passwords do not match"
                        else -> ""
                    }
                    confirmPasswordError = confirmPasswordErrorMessage.isNotEmpty()
                    onPasswordError(confirmPasswordError)
                    // Only validate confirm password if the main password is not empty
                    if (userProfile.value.password.isNotEmpty()) {
                        confirmPasswordErrorMessage = if (it != userProfile.value.password) {
                            "Passwords do not match"
                        } else {
                            ""
                        }
                    } else {
                        confirmPasswordErrorMessage =
                            if (it.isEmpty()) "Confirm Password is required" else ""
                    }
                    confirmPasswordError = confirmPasswordErrorMessage.isNotEmpty()
                    onPasswordError(confirmPasswordError) // Trigger callback for password error state
                },
                label = { Text("Confirm Password") },
                placeholder = { Text("Re-enter password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                isError = confirmPasswordErrorMessage.isNotEmpty()
            )
            if (confirmPasswordErrorMessage.isNotEmpty()) {
                Text(confirmPasswordErrorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Year of birth using DropdownMenuField
            DropdownMenuField(userProfile.value.yob) { selectedYear ->
                userProfile.value = userProfile.value.copy(yob = selectedYear)
                yearOfBirthError = if (selectedYear.isEmpty()) "Year of birth is required" else ""
            }
            if (yearOfBirthError.isNotEmpty()) {
                Text(yearOfBirthError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Gender selection using GenderSelection
            GenderSelection(userProfile.value.gender) { selectedGender ->
                userProfile.value = userProfile.value.copy(gender = selectedGender)
            }
        }

        item {
            OutlinedTextField(
                value = userProfile.value.mobile,
                onValueChange = {
                    userProfile.value = userProfile.value.copy(mobile = it)
                    mobileNumberError = when {
                        it.isEmpty() -> "Mobile number is required"
                        !it.matches(mobileNumberPattern) -> "Mobile number must be 8 digits"
                        else -> ""
                    }
                    onMobileNumberError(mobileNumberError)
                },
                label = {
                    Text(buildAnnotatedString {
                        append("Mobile Number")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    })
                },
                placeholder = { Text("Enter mobile number") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                isError = mobileNumberError.isNotEmpty()
            )
            if (mobileNumberError.isNotEmpty()) {
                Text(mobileNumberError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            OutlinedTextField(
                value = userProfile.value.email,
                onValueChange = {
                    userProfile.value = userProfile.value.copy(email = it)
                    emailError = when {
                        it.isEmpty() -> "Email is required"
                        !it.matches(emailPattern) -> "Invalid email format"
                        else -> ""
                    }
                    onEmailError(emailError)
                },
                label = {
                    Text(buildAnnotatedString {
                        append("Email")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    })
                },
                placeholder = { Text("Enter email") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                isError = emailError.isNotEmpty()
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
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
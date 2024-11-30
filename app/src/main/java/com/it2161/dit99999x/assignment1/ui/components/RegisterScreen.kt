package com.it2161.dit99999x.assignment1.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.it2161.dit99999x.assignment1.MovieRaterApplication
import com.it2161.dit99999x.assignment1.data.UserProfile
import java.util.*

@Composable
fun RegisterUserScreen(navController: NavController) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var receiveUpdates by remember { mutableStateOf(false) }
    var yearOfBirth by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Validation states for password fields
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Validation states for other fields
    var userNameError by remember { mutableStateOf("") }
    var mobileNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var yearOfBirthError by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf("") }

    // Validation patterns for each rule
    val usernamePattern = "^[a-zA-Z0-9]{3,12}$".toRegex() // Alphanumeric, 3-12 characters
    val mobileNumberPattern = "^\\d{8}$".toRegex() // Exactly 8 digits
    val emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex() // Basic email pattern

    // Validation function for required fields
    fun validateFields(): Boolean {
        var isValid = true

        // Username validation
        userNameError = when {
            userName.isEmpty() -> "Username is required"
            !userName.matches(usernamePattern) -> "Username must be 3-12 characters, alphanumeric only"
            else -> ""
        }
        if (userNameError.isNotEmpty()) isValid = false

        // Mobile number validation
        mobileNumberError = when {
            mobileNumber.isEmpty() -> "Mobile number is required"
            !mobileNumber.matches(mobileNumberPattern) -> "Mobile number must be 8 digits"
            else -> ""
        }
        if (mobileNumberError.isNotEmpty()) isValid = false

        // Email validation
        emailError = when {
            email.isEmpty() -> "Email is required"
            !email.matches(emailPattern) -> "Invalid email format"
            else -> ""
        }
        if (emailError.isNotEmpty()) isValid = false

        // Year of birth validation
        yearOfBirthError = if (yearOfBirth.isEmpty()) "Year of birth is required" else ""
        if (yearOfBirthError.isNotEmpty()) isValid = false

        // Gender validation
        genderError = if (gender.isEmpty()) "Gender is required" else ""
        if (genderError.isNotEmpty()) isValid = false

        // Password validation
        passwordError = when {
            password.length < 8 -> "Password must be at least 8 characters long"
            !password.any { it.isUpperCase() } -> "Password must contain at least 1 uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least 1 lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least 1 number"
            !password.any { "!@#$%^&*()_+-=<>?/.,;:'\"".contains(it) } -> "Password must contain at least 1 special character"
            else -> ""
        }
        if (passwordError.isNotEmpty()) isValid = false

        // Confirm password validation
        confirmPasswordError = if (confirmPassword != password) "Passwords do not match" else ""
        if (confirmPasswordError.isNotEmpty()) isValid = false // Prevent registration if passwords don't match
        return isValid
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(top = 80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Username input with validation
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    userNameError = when {
                        it.isEmpty() -> "Username is required"
                        !it.matches(usernamePattern) -> "Username must be 3-12 characters, alphanumeric only"
                        else -> ""
                    }
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
                isError = userNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            )
            if (userNameError.isNotEmpty()) {
                Text(userNameError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Password input with validation
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.length < 8 -> "Password must be at least 8 characters long"
                        !it.any { char -> char.isUpperCase() } -> "Password must contain at least 1 uppercase letter"
                        !it.any { char -> char.isLowerCase() } -> "Password must contain at least 1 lowercase letter"
                        !it.any { char -> char.isDigit() } -> "Password must contain at least 1 number"
                        !it.any { char -> "!@#$%^&*()_+-=<>?/.,;:'\"".contains(char) } -> "Password must contain at least 1 special character"
                        else -> ""
                    }
                },
                label = {
                    Text(buildAnnotatedString {
                        append("Password")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    })
                },
                placeholder = { Text("Enter password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Confirm Password input with validation
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError =
                        if (confirmPassword != password) "Passwords do not match" else ""
                },
                label = {
                    Text(buildAnnotatedString {
                        append("Confirm Password")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    })
                },
                placeholder = { Text("Enter password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                isError = confirmPasswordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(confirmPasswordError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Year of birth input with validation
            DropdownMenuField(yearOfBirth) { selectedYear ->
                yearOfBirth = selectedYear
                yearOfBirthError = if (selectedYear.isEmpty()) "Year of birth is required" else ""
            }
            if (yearOfBirthError.isNotEmpty()) {
                Text(yearOfBirthError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Gender selection with validation
            GenderSelection(gender) { selectedGender ->
                gender = selectedGender
                genderError = if (selectedGender.isEmpty()) "Gender is required" else ""
            }
            if (genderError.isNotEmpty()) {
                Text(genderError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Mobile number input with validation
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = {
                    mobileNumber = it
                    mobileNumberError = if (it.length != 8) "Mobile number must be 8 digits" else ""
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = mobileNumberError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            )
            if (mobileNumberError.isNotEmpty()) {
                Text(mobileNumberError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Email input with validation
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (!it.matches(emailPattern)) "Invalid email format" else ""
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
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
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
                    checked = receiveUpdates,
                    onCheckedChange = { receiveUpdates = it }
                )
                Text(text = "To receive updates")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Register button with validation
            Button(
                onClick = {
                    if (validateFields()) {
                        // Handle registration logic (successful)
                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT)
                            .show()
                        MovieRaterApplication.instance.userProfile = UserProfile(
                            userName,
                            password,
                            email,
                            gender,
                            mobileNumber,
                            receiveUpdates,
                            yearOfBirth
                        )
                        navController.navigate("landing")
                    } else {
                        Toast.makeText(
                            context,
                            "Please fix the errors before proceeding.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(1.dp))
        }

        item {
            // Cancel and navigate back to Login screen
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
            ) {
                Text("Cancel")
            }
        }

    }
}

@Composable
fun GenderSelection(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rotated "Gender" label on the left side
        Text(
            text = buildAnnotatedString {
                append("Gender")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .rotate(-90f)
                .padding(end = 16.dp) // Space between label and options
        )

        // Gender options in a column
        Column {
            listOf("Male", "Female", "Non-Binary", "Prefer not to say").forEach { genderOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp, horizontal = 25.dp) // Space between each option row
                ) {
                    Text(
                        text = genderOption,
                        modifier = Modifier.weight(1f) // Text takes available space
                    )
                    RadioButton(
                        selected = selectedGender == genderOption,
                        onClick = { onGenderSelected(genderOption) }
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownMenuField(selectedYear: String, onYearSelected: (String) -> Unit) {
    val years = (1920..Calendar.getInstance().get(Calendar.YEAR)).map { it.toString() }
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedYear,
            onValueChange = { },
            readOnly = true,
            label = { Text(buildAnnotatedString {
                append("Year of Birth")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }) },
            placeholder = { Text("Select Year of Birth") },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 15.dp),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand Year of Birth")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    },
                    text = { Text(text = year) }
                )

            }
        }
    }
}

@Preview
@Composable
fun RegisterUserScreenPreview() {
    RegisterUserScreen(navController = rememberNavController())
}

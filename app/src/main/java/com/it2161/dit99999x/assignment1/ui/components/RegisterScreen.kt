package com.it2161.dit99999x.assignment1.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium, modifier = Modifier
            .padding(top = 80.dp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            placeholder = { Text("Enter username") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            placeholder = { Text("Enter password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuField(yearOfBirth) { selectedYear -> yearOfBirth = selectedYear }

        Spacer(modifier = Modifier.height(8.dp))

        GenderSelection(gender) { selectedGender -> gender = selectedGender }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { newValue ->
                mobileNumber = newValue.filter { it.isDigit() }
            },
            label = { Text("Mobile Number") },
            placeholder = { Text("Enter mobile number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = receiveUpdates,
                onCheckedChange = { receiveUpdates = it }
            )
            Text(text = "Receive updates via email")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validateFields(userName, password, confirmPassword, email, gender, mobileNumber, yearOfBirth)) {
                    // Clear existing profile
                    MovieRaterApplication.instance.userProfile = null

                    // Create new profile
                    val newProfile = UserProfile(userName, password, email, gender, mobileNumber, receiveUpdates, yearOfBirth)
                    MovieRaterApplication.instance.userProfile = newProfile

                    navController.navigate("landing")
                } else {
                    // Display error message using Toast
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()

                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
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
            text = "Gender",
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
            label = { Text("Year of Birth") },
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

fun validateFields(
    userName: String,
    password: String,
    confirmPassword: String,
    email: String,
    gender: String,
    mobileNumber: String,
    yearOfBirth: String
): Boolean {
    return userName.isNotEmpty() && password == confirmPassword && email.isNotEmpty() && gender.isNotEmpty() && mobileNumber.isNotEmpty() && yearOfBirth.isNotEmpty()
}

@Preview
@Composable
fun RegisterUserScreenPreview() {
    RegisterUserScreen(navController = rememberNavController())
}

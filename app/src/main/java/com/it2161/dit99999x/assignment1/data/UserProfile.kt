package com.it2161.dit99999x.assignment1.data
import com.it2161.dit99999x.assignment1.R

data class UserProfile(
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    val gender: String = "",
    val mobile: String = "",
    val updates: Boolean = false,
    val yob: String = "",
    val profilePicture: Int = R.drawable.avatar_3 // Default profile picture
)

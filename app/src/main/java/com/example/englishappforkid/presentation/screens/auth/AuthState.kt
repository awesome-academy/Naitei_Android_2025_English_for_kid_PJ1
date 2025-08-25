package com.example.englishappforkid.presentation.screens.auth

import com.example.englishappforkid.data.model.UserProfile

data class AuthState(
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: UserProfile? = null,
)

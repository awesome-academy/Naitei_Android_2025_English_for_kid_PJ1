package com.example.englishappforkid.presentation.screens.auth

data class AuthState(
    val email: String = "",
    val pass: String = "",
    val confirmPass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

package com.example.englishappforkid.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.englishappforkid.data.DataManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModelFactory(
    private val dataManager: DataManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Cung cấp các instance của Firebase
            return AuthViewModel(
                dataManager,
                FirebaseAuth.getInstance(),
                FirebaseFirestore.getInstance(),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

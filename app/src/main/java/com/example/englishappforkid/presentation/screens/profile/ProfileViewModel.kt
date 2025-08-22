package com.example.englishappforkid.presentation.screens.profile

import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        // We observe the authentication state.
        // This makes sure the ViewModel reacts when a user signs in or out.
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                fetchUserProfile()
            } else {
                // If there is no user, we reset the profile data to null.
                _userProfile.value = null
            }
        }
    }

    private fun fetchUserProfile() {
        val uid = auth.currentUser?.uid
        // If there's no UID, we shouldn't try to fetch.
        if (uid == null) {
            _userProfile.value = null
            return
        }

        db.collection("user_profiles").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    _userProfile.update { profile }
                } else {
                    // Handle case where profile document doesn't exist
                    _userProfile.value = UserProfile(uid = uid, email = auth.currentUser?.email ?: "")
                }
            }
            .addOnFailureListener { e ->
                // Handle fetch failure
            }
    }
}
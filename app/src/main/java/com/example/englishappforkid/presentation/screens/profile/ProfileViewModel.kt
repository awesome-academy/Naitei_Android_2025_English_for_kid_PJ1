package com.example.englishappforkid.presentation.screens.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishappforkid.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import java.util.UUID

class ProfileViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val context = application.applicationContext

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating = _isUpdating.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut = _isLoggedOut.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                fetchUserProfile()
            } else {
                _userProfile.value = null
            }
        }
    }

    private fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        db
            .collection("user_profiles")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _userProfile.value = document.toObject(UserProfile::class.java)
                } else {
                    _userProfile.value =
                        UserProfile(
                            uid = uid,
                            email = auth.currentUser?.email ?: "",
                        )
                }
            }.addOnFailureListener {
                _userProfile.value =
                    UserProfile(
                        uid = uid,
                        email = auth.currentUser?.email ?: "",
                    )
            }
    }

    fun updateProfile(
        address: String,
        nickname: String,
        fullname: String,
        age: String,
        newAvatarUri: Uri? = null,
    ) {
        val uid = auth.currentUser?.uid ?: return
        val currentProfile = _userProfile.value ?: return

        viewModelScope.launch {
            _isUpdating.value = true
            try {
                val avatarUrl =
                    if (newAvatarUri != null) {
                        uploadAvatar(uid, newAvatarUri)
                    } else {
                        currentProfile.avatarUrl
                    }

                val updatedProfile =
                    currentProfile.copy(
                        address = address,
                        nickname = nickname,
                        fullname = fullname,
                        age = age,
                        avatarUrl = avatarUrl,
                        email = currentProfile.email.ifEmpty { auth.currentUser?.email ?: "" },
                    )

                saveProfile(uid, updatedProfile)
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isUpdating.value = false
            }
        }
    }

    private suspend fun uploadAvatar(
        uid: String,
        uri: Uri,
    ): String {
        val fileName = "avatars/${uid}_${UUID.randomUUID()}.jpg"
        val ref = storage.reference.child(fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            ref.putStream(inputStream).await()
        } ?: throw Exception("Error")

        return ref.downloadUrl.await().toString()
    }

    private fun getBytesFromUri(uri: Uri): ByteArray {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: throw Exception("Can't read image")
    }

    private suspend fun saveProfile(
        uid: String,
        profile: UserProfile,
    ) {
        db
            .collection("user_profiles")
            .document(uid)
            .set(profile)
            .await()
    }

    fun logout() {
        auth.signOut()
        _isLoggedOut.value = true
    }
}

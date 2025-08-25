
import androidx.lifecycle.ViewModel
import com.example.englishappforkid.data.model.UserProfile
import com.example.englishappforkid.presentation.screens.auth.AuthState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(pass: String) {
        _uiState.update { it.copy(pass = pass) }
    }

    fun onConfirmPasswordChange(confirmPass: String) {
        _uiState.update { it.copy(confirmPass = confirmPass) }
    }

    // ✅ Đăng ký tài khoản mới
    fun signUp(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        if (_uiState.value.pass != _uiState.value.confirmPass) {
            _uiState.update { it.copy(isLoading = false, error = "Passwords do not match.") }
            return
        }
        if (_uiState.value.pass.length < 6) {
            _uiState.update { it.copy(isLoading = false, error = "Password must be at least 6 characters.") }
            return
        }

        auth
            .createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserProfileIfNew(auth.currentUser?.uid, auth.currentUser?.email)
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(error = task.exception?.message ?: "Sign up failed.")
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
    }

    // ✅ Đăng nhập
    fun signIn(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        if (_uiState.value.email.isBlank() || _uiState.value.pass.isBlank()) {
            _uiState.update { it.copy(isLoading = false, error = "Email and password cannot be empty.") }
            return
        }

        auth
            .signInWithEmailAndPassword(_uiState.value.email, _uiState.value.pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Không overwrite profile, chỉ fetch
                    fetchUserProfile(auth.currentUser?.uid)
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(error = task.exception?.message ?: "Sign in failed.")
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
    }

    // ✅ Đăng nhập Google
    fun signInWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
    ) {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserProfileIfNew(auth.currentUser?.uid, auth.currentUser?.email)
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(error = task.exception?.message ?: "Google Sign In failed.")
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
    }

    // ✅ Chỉ tạo mới nếu chưa có
    private fun saveUserProfileIfNew(
        uid: String?,
        email: String?,
    ) {
        if (uid == null || email == null) return

        val docRef = db.collection("user_profiles").document(uid)
        docRef
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    val userProfile =
                        UserProfile(
                            uid = uid,
                            email = email,
                            fullname = "",
                            address = "",
                            nickname = "",
                            age = "",
                            avatarUrl = "",
                        )
                    docRef.set(userProfile)
                }
            }.addOnFailureListener { e ->
                _uiState.update { it.copy(error = "Failed to check user profile: ${e.message}") }
            }
    }

    // ✅ Fetch dữ liệu từ Firestore khi login
    private fun fetchUserProfile(uid: String?) {
        if (uid == null) return

        db
            .collection("user_profiles")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val profile = document.toObject(UserProfile::class.java)
                if (profile != null) {
                    _uiState.update { it.copy(currentUser = profile) }
                }
            }.addOnFailureListener { e ->
                _uiState.update { it.copy(error = "Failed to load user profile: ${e.message}") }
            }
    }
}

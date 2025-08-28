package com.example.englishappforkid.presentation.screens.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.englishappforkid.data.DataManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockDataManager: DataManager
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockDb: FirebaseFirestore

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDataManager = mock()
        mockAuth = mock()
        mockDb = mock()
        viewModel = AuthViewModel(mockDataManager, mockAuth, mockDb)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // KIỂM TRA: Cập nhật state email khi người dùng nhập liệu.
    @Test
    fun `onEmailChange should update uiState with correct email`() =
        runTest {
            val testEmail = "test@example.com"
            viewModel.onEmailChange(testEmail)
            val actualEmail = viewModel.uiState.value.email
            assertEquals(testEmail, actualEmail)
        }

    // KIỂM TRA: Báo lỗi khi người dùng đăng nhập với email hoặc mật khẩu rỗng.
    @Test
    fun `signIn with empty email should set error state`() =
        runTest {
            viewModel.onEmailChange("")
            viewModel.onPasswordChange("")
            viewModel.signIn(rememberMe = false) {}
            val expectedError = "Email and password cannot be empty."
            val actualError = viewModel.uiState.value.error
            assertEquals(expectedError, actualError)
        }

    // KIỂM TRA: Báo lỗi khi người dùng đăng ký với mật khẩu không khớp.
    @Test
    fun `signUp with mismatched passwords should set error state`() =
        runTest {
            viewModel.onPasswordChange("123456")
            viewModel.onConfirmPasswordChange("654321")
            viewModel.signUp {}
            val expectedError = "Passwords do not match."
            val actualError = viewModel.uiState.value.error
            assertEquals(expectedError, actualError)
        }

    // KIỂM TRA: Báo lỗi khi người dùng đăng ký với mật khẩu quá ngắn.
    @Test
    fun `signUp with short password should set error state`() =
        runTest {
            viewModel.onPasswordChange("123")
            viewModel.onConfirmPasswordChange("123")
            viewModel.signUp {}
            val expectedError = "Password must be at least 6 characters."
            val actualError = viewModel.uiState.value.error
            assertEquals(expectedError, actualError)
        }

    // KIỂM TRA: Báo lỗi khi người dùng quên mật khẩu mà chưa nhập email.
    @Test
    fun `sendPasswordResetEmail with empty email should set error state`() =
        runTest {
            viewModel.onEmailChange("")
            viewModel.sendPasswordResetEmail {}
            val expectedError = "Please enter your email."
            val actualError = viewModel.uiState.value.error
            assertEquals(expectedError, actualError)
        }

    // KIỂM TRA HÀNH VI: `saveCredentials` phải được gọi khi rememberMe = true.
    @Test
    fun `saveOrClearCredentials with true should call dataManager saveCredentials`() =
        runTest {
            val testEmail = "test@example.com"
            val testPass = "123456"
            viewModel.onEmailChange(testEmail)
            viewModel.onPasswordChange(testPass)

            viewModel.saveOrClearCredentials(true)
            testDispatcher.scheduler.advanceUntilIdle()

            verify(mockDataManager).saveCredentials(testEmail, testPass)
        }

    // KIỂM TRA HÀNH VI: `clearCredentials` phải được gọi khi rememberMe = false.
    @Test
    fun `saveOrClearCredentials with false should call dataManager clearCredentials`() =
        runTest {
            viewModel.saveOrClearCredentials(false)
            testDispatcher.scheduler.advanceUntilIdle()

            verify(mockDataManager).clearCredentials()
        }

    // KIỂM TRA LUỒNG THÀNH CÔNG: Callback `onSuccess` phải được gọi khi Firebase báo đăng nhập thành công.
    @Test
    fun `signIn with correct credentials should call onSuccess callback`() =
        runTest {
            val email = "test@example.com"
            val pass = "123456"
            viewModel.onEmailChange(email)
            viewModel.onPasswordChange(pass)

            val mockTask: Task<AuthResult> = mock()
            whenever(mockAuth.signInWithEmailAndPassword(email, pass)).thenReturn(mockTask)

            whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
                val successfulTask: Task<AuthResult> =
                    mock {
                        on { isSuccessful } doReturn true
                    }
                listener.onComplete(successfulTask)
                mockTask
            }

            var onSuccessCalled = false
            viewModel.signIn(rememberMe = false) {
                onSuccessCalled = true
            }

            assertTrue("onSuccess callback should be called on successful sign in", onSuccessCalled)
        }

    // KIỂM TRA LUỒNG THẤT BẠI: State lỗi phải được cập nhật khi Firebase báo đăng nhập thất bại.
    @Test
    fun `signIn with incorrect credentials should set error state`() =
        runTest {
            val email = "wrong@example.com"
            val pass = "wrongpass"
            viewModel.onEmailChange(email)
            viewModel.onPasswordChange(pass)
            val errorMessage = "Invalid credentials"

            val mockTask: Task<AuthResult> = mock()
            whenever(mockAuth.signInWithEmailAndPassword(email, pass)).thenReturn(mockTask)

            whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<AuthResult>>(0)
                val failedTask: Task<AuthResult> =
                    mock {
                        on { isSuccessful } doReturn false
                        on { exception } doReturn Exception(errorMessage)
                    }
                listener.onComplete(failedTask)
                mockTask
            }

            viewModel.signIn(rememberMe = false) {}

            assertEquals(errorMessage, viewModel.uiState.value.error)
        }

    // KIỂM TRA LUỒNG THÀNH CÔNG: Callback onSuccess phải được gọi khi gửi email reset thành công.
    @Test
    fun `sendPasswordResetEmail with valid email should call onSuccess`() =
        runTest {
            val email = "user@example.com"
            viewModel.onEmailChange(email)

            val mockTask: Task<Void> = mock()
            whenever(mockAuth.sendPasswordResetEmail(email)).thenReturn(mockTask)

            whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<Void>>(0)
                val successfulTask: Task<Void> = mock { on { isSuccessful } doReturn true }
                listener.onComplete(successfulTask)
                mockTask
            }

            var onSuccessCalled = false
            viewModel.sendPasswordResetEmail {
                onSuccessCalled = true
            }

            assertTrue("onSuccess should be called for successful password reset", onSuccessCalled)
        }

    // KIỂM TRA LUỒNG THẤT BẠI: State lỗi phải được cập nhật khi gửi email reset thất bại.
    @Test
    fun `sendPasswordResetEmail when firebase fails should set error state`() =
        runTest {
            val email = "user@example.com"
            viewModel.onEmailChange(email)
            val errorMessage = "Firebase error"

            val mockTask: Task<Void> = mock()
            whenever(mockAuth.sendPasswordResetEmail(email)).thenReturn(mockTask)

            whenever(mockTask.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<Void>>(0)
                val failedTask: Task<Void> =
                    mock {
                        on { isSuccessful } doReturn false
                        on { exception } doReturn Exception(errorMessage)
                    }
                listener.onComplete(failedTask)
                mockTask
            }

            viewModel.sendPasswordResetEmail {}

            assertEquals(errorMessage, viewModel.uiState.value.error)
        }
}

package ru.foody.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel

import ru.foody.R
import ru.foody.api.UserRepository
import ru.foody.dto.auth.AuthResponse

class LoginViewModel(private val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun tryLoginUsingExistingToken() {
        if (userRepository.authToken != null) {
            loginUsingExistingToken()
        }
    }

    fun onLogin(authResponse: AuthResponse?) {
        if (authResponse != null) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = authResponse.login))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun login(username: String, password: String) {
        userRepository.login(username, password) { authResponse: AuthResponse? ->
            onLogin(authResponse)
        }
    }

    fun loginUsingExistingToken() {
        userRepository.loginUsingExistingToken { authResponse: AuthResponse? ->
            onLogin(authResponse)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}
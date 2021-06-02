package ru.foody.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.foody.api.UserRepository

class LoginViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                userRepository = UserRepository(
                    context.applicationContext as Application
                ),
                context.applicationContext as Application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
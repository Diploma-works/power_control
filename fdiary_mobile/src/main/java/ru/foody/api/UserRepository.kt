package ru.foody.api

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.foody.dto.auth.AuthRequest
import ru.foody.dto.auth.AuthResponse
import ru.foody.SharedPrefsConfiguration.CONFIG_NAME
import ru.foody.SharedPrefsConfiguration.TOKEN_FIELD_NAME
import ru.foody.SharedPrefsConfiguration.USERNAME_FIELD_NAME

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class UserRepository(private val applicationContext: Context) {
    var authToken: String? = null

    // in-memory cache of user
    var successfulAuthResult: AuthResponse? = null
        private set

    val isLoggedIn: Boolean
        get() = successfulAuthResult != null

    init {
        initTokenFromPrefs()
        DAO.userRepository = this
    }

    fun initTokenFromPrefs() {
        authToken = applicationContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE)
            .getString(TOKEN_FIELD_NAME, null)
    }

    fun logout() {
        successfulAuthResult = null
        applicationContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE).edit()
            .remove(TOKEN_FIELD_NAME).remove(USERNAME_FIELD_NAME).apply()
        authToken = null
    }

    private fun setToken(token: String) {
        authToken = token
        applicationContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE).edit()
            .putString(TOKEN_FIELD_NAME, token).apply()
    }

    private fun setLoggedInUser(authResponse: AuthResponse) {
        this.successfulAuthResult = authResponse
        setToken(authResponse.token)
    }

    fun login(
        login: String,
        password: String,
        callback: (authResponse: AuthResponse?) -> (Unit)
    ) {
        DAO.loginApi.login(AuthRequest(login, password))
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (!response.isSuccessful || response.body() == null) {
                        callback(null)
                    } else {
                        setLoggedInUser(response.body()!!)
                        callback(successfulAuthResult!!)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback(null)
                }
            })
    }

    fun loginUsingExistingToken(
        callback: (authResponse: AuthResponse?) -> (Unit)
    ) {
        DAO.loginApi.token().enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (!response.isSuccessful || response.body() == null) {
                    callback(null)
                } else {
                    setLoggedInUser(response.body()!!)
                    callback(successfulAuthResult!!)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

}
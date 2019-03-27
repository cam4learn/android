package io.github.gubarsergey.cam4learn.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.gubarsergey.cam4learn.R
import io.github.gubarsergey.cam4learn.utility.extension.input
import io.github.gubarsergey.cam4learn.utility.extension.navigate
import io.github.gubarsergey.cam4learn.utility.helper.SharedPrefHelper
import io.github.gubarsergey.cam4learn.utility.validator.CredentialsValidator
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkIfUserIsLoggedIn { navigate<MainActivity>(this) }
        setupListeners()
    }

    private fun checkIfUserIsLoggedIn(isUserLoggedAction: () -> Unit) {
        if (SharedPrefHelper.isUserLoggedIn(this)) isUserLoggedAction.invoke()
    }

    private fun setupListeners() {
        login_login_button.setOnClickListener {
            handleLogin(login_email_edit_text.input, login_password_edit_text.input)
        }
    }

    private fun handleLogin(email: String, password: String) {
        val isEmailValid = CredentialsValidator.isEmailValid(email)
        val isPasswordValid = CredentialsValidator.isPasswordValid(password)
        if (isEmailValid && isPasswordValid) {
            // TODO: Network request
            SharedPrefHelper.setUserLoggedIn(this, true).also { navigate<MainActivity>(this) }
            return
        }
        handleLoginError(isEmailValid, isPasswordValid)
    }

    private fun handleLoginError(emailValid: Boolean, passwordValid: Boolean) {
        Timber.d("handleLoginError: emailValid = [$emailValid], passwordValid = [$passwordValid]")
        if (!emailValid) {
            login_email_text_input_layout.error = getString(R.string.error_email_validation)
        }
        if (!passwordValid) {
            login_password_text_input_layout.error = getString(R.string.error_password_validation)
        }
    }
}
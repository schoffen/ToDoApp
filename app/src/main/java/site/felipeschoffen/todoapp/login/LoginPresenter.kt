package site.felipeschoffen.todoapp.login

import android.util.Patterns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.InputErrors
import site.felipeschoffen.todoapp.common.database.DatabaseCallback
import site.felipeschoffen.todoapp.common.database.DatabaseError

class LoginPresenter(override val view: Login.View, private val coroutineScope: CoroutineScope) : Login.Presenter {

    override fun login(email: String, password: String) {
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        if (isEmailValid && isPasswordValid) {
            coroutineScope.launch {
                val loginResult = DataSource.login(email, password)

                if (loginResult.success)
                    view.goToMainScreen()
                else
                    displayDatabaseError(loginResult.error)
            }
        }
    }

    private fun displayDatabaseError(error: DatabaseError?) {
        when (error) {
            DatabaseError.INVALID_CREDENTIALS -> {
                view.displayEmailError(InputErrors.INCORRECT_INFO)
                view.displayPasswordError(InputErrors.INCORRECT_INFO)
            }

            DatabaseError.FAILED_TO_SET_USER_INFO -> {

            }

            else -> { }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            view.displayEmailError(InputErrors.FIELD_EMPTY)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.displayEmailError(InputErrors.EMAIL_FORMAT_INVALID)
            false
        } else {
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            view.displayPasswordError(InputErrors.FIELD_EMPTY)
            false
        } else {
            true
        }
    }
}
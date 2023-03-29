package site.felipeschoffen.todoapp.login

import android.util.Patterns
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.InputErrors
import site.felipeschoffen.todoapp.common.database.DatabaseCallback
import site.felipeschoffen.todoapp.common.database.DatabaseError

class LoginPresenter(override val view: Login.View) : Login.Presenter {

    override fun login(email: String, password: String) {
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        if (isEmailValid && isPasswordValid) {
            DataSource.login(email, password, object : DatabaseCallback {
                override fun onSuccess() {
                    view.goToMainScreen()
                }

                override fun onFailure(error: DatabaseError) {
                    checkDatabaseError(error)
                }

                override fun onComplete() {

                }
            })
        }
    }

    private fun checkDatabaseError(error: DatabaseError) {
        when (error) {
            DatabaseError.INVALID_CREDENTIALS -> {
                view.displayEmailError(InputErrors.INCORRECT_INFO)
                view.displayPasswordError(InputErrors.INCORRECT_INFO)
            }
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
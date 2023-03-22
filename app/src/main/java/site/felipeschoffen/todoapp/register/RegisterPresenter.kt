package site.felipeschoffen.todoapp.register

import android.util.Patterns
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.DataSource

class RegisterPresenter(val view: Register.View?) : Register.Presenter {

    override fun register(email: String, password: String, confirmPassword: String) {

        if (validateData(email, password, confirmPassword)) {
            DataSource.register(email, password, object : Callback{
                override fun onSuccess() {
                }

                override fun onFailure(message: String) {
                    if (message == "The email address is already in use by another account.") {
                        view?.displayEmailError(RegisterErrors.EMAIL_ALREADY_IN_USE)
                    }
                }

                override fun onComplete() {
                }
            })
        }
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        val emailValid = validateEmail(email)
        val passwordsValid = validatePasswords(password, confirmPassword)

        return emailValid && passwordsValid
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            view?.displayEmailError(RegisterErrors.FIELD_EMPTY)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view?.displayEmailError(RegisterErrors.EMAIL_FORMAT_INVALID)
            false
        } else {
            true
        }
    }

    private fun validatePasswords(password: String, confirmPassword: String): Boolean {
        return if (password.isEmpty()) {
            view?.displayPasswordError(RegisterErrors.FIELD_EMPTY)
            false
        } else if (confirmPassword.isEmpty()) {
            view?.displayConfirmPasswordError(RegisterErrors.FIELD_EMPTY)
            false
        } else if (password.length < Constants.PASSWORD_MIN_LENGTH) {
            view?.displayPasswordError(RegisterErrors.PASSWORD_SHORT_LENGTH)
            false
        } else if (confirmPassword.length < Constants.PASSWORD_MIN_LENGTH) {
            view?.displayConfirmPasswordError(RegisterErrors.PASSWORD_SHORT_LENGTH)
            false
        } else if (password != confirmPassword) {
            view?.displayPasswordError(RegisterErrors.PASSWORD_NOT_MATCH)
            view?.displayConfirmPasswordError(RegisterErrors.PASSWORD_NOT_MATCH)
            false
        } else {
            true
        }
    }

}
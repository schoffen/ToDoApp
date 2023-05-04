package site.felipeschoffen.todoapp.register

import android.util.Patterns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.InputErrors

class RegisterPresenter(override val view: Register.View, private val coroutineScope: CoroutineScope) : Register.Presenter {

    override fun register(name: String, email: String, password: String, confirmPassword: String) {
        view.displayRegisterButtonProgress(true)

        if (validateData(name, email, password, confirmPassword)) {
            coroutineScope.launch {
                val registerResult = DataSource.register(name, email, password)
                if (registerResult.success) {
                    view.goToMainScreen()
                    view.displayRegisterButtonProgress(false)
                }
                else {
                    // view.displayEmailError()
                    // TRATAR ESSA EXCESSAO
                }
            }
        } else {
            view.displayRegisterButtonProgress(false)
        }
    }

    private fun validateData(name: String, email: String, password: String, confirmPassword: String): Boolean {
        val nameValid = validateName(name)
        val emailValid = validateEmail(email)
        val passwordValid = validatePassword(password, view::displayPasswordError)
        val confirmPasswordValid = validatePassword(confirmPassword, view::displayConfirmPasswordError)
        val passwordMatch = if (passwordValid && confirmPasswordValid) isPasswordsEqual(password, confirmPassword) else false

        return nameValid && emailValid && passwordMatch
    }

    private fun validateName(name: String): Boolean {
        return if (name.isEmpty()) {
            view.displayNameError(InputErrors.FIELD_EMPTY)
            false
        } else if (name.length < Constants.NAME_MIN_LENGTH) {
            view.displayNameError(InputErrors.NAME_SHORT_LENGHT)
            false
        } else {
            true
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

    private fun validatePassword(password: String, viewDisplayError: (InputErrors) -> Unit): Boolean {
        return if (password.isEmpty()) {
            viewDisplayError(InputErrors.FIELD_EMPTY)
            false
        } else if (password.length < Constants.PASSWORD_MIN_LENGTH) {
            viewDisplayError(InputErrors.PASSWORD_SHORT_LENGTH)
            false
        } else {
            true
        }
    }

    private fun isPasswordsEqual(password: String, confirmPassword: String): Boolean {
        return if (password == confirmPassword) {
            true
        } else {
            view.displayPasswordError(InputErrors.PASSWORD_NOT_MATCH)
            view.displayConfirmPasswordError(InputErrors.PASSWORD_NOT_MATCH)
            false
        }
    }

}
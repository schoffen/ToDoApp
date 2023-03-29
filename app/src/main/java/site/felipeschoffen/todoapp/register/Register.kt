package site.felipeschoffen.todoapp.register

import site.felipeschoffen.todoapp.common.InputErrors

interface Register {
    interface View {
        val presenter: Register.Presenter
        fun displayNameError(error: InputErrors?)
        fun displayEmailError(error: InputErrors?)
        fun displayPasswordError(error: InputErrors?)
        fun displayConfirmPasswordError(error: InputErrors?)
        fun displayRegisterButtonProgress(show: Boolean)
        fun goToMainScreen()
    }

    interface Presenter {
        val view: View
        fun register(name: String, email: String, password: String, confirmPassword: String)
    }
}
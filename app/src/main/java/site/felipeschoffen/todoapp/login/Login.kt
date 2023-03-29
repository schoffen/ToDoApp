package site.felipeschoffen.todoapp.login

import site.felipeschoffen.todoapp.common.InputErrors

interface Login {
    interface View {
        val presenter: Login.Presenter
        fun displayEmailError(error: InputErrors?)
        fun displayPasswordError(error: InputErrors?)
        fun displayLoginButtonProgress(show: Boolean)
        fun goToMainScreen()
    }

    interface Presenter {
        val view: Login.View
        fun login(email: String, password: String)
    }
}
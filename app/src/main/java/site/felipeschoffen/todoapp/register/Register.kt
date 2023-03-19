package site.felipeschoffen.todoapp.register

interface Register {
    interface View {
        fun displayEmailError(error: RegisterErrors)
        fun displayPasswordError(error: RegisterErrors)
    }

    interface Presenter {
        fun register(email: String, password: String, confirmPassword: String)
    }
}
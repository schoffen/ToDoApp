package site.felipeschoffen.todoapp.main

interface Main {
    interface View {
        fun goToSplashScreen()
    }

    interface Presenter {
        val view: Main.View
        fun logout()
    }
}
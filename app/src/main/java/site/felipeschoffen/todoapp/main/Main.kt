package site.felipeschoffen.todoapp.main

import android.content.Context
import androidx.fragment.app.FragmentManager

interface Main {
    interface View {
        fun goToSplashScreen()
        fun showSnackBar(message: String)
    }

    interface Presenter {
        val view: Main.View
        fun logout()
        fun openCreateTaskDialog(supportFragmentManager: FragmentManager, context: Context)
    }
}
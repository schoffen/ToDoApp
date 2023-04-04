package site.felipeschoffen.todoapp.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.dialogs.CustomDialog

class MainPresenter(override val view: Main.View) : Main.Presenter {
    override fun logout() {
        DataSource.logout(object : Callback {
            override fun onSuccess() {

            }

            override fun onFailure(message: String) {

            }

            override fun onComplete() {
                view.goToSplashScreen()
            }
        })
    }

    override fun openCreateTaskDialog(supportFragmentManager: FragmentManager, context: Context) {
        CustomDialog.CreateTaskDialog(supportFragmentManager, object : Callback {
            override fun onSuccess() {
                view.showSnackBar(context.getString(R.string.task_create_successfully))
            }

            override fun onFailure(message: String) {
                view.showSnackBar(context.getString(R.string.task_create_error))
            }

            override fun onComplete() {

            }
        })
            .show(supportFragmentManager, "")
    }
}
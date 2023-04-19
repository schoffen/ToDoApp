package site.felipeschoffen.todoapp.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CoroutineScope
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.dialogs.CustomDialog

class MainPresenter(override val view: Main.View, private val coroutineScope: CoroutineScope) : Main.Presenter {
    override fun logout() {
        DataSource.logout()
        view.goToSplashScreen()
    }

    override fun openCreateTaskDialog(supportFragmentManager: FragmentManager, context: Context) {
        CustomDialog.CreateTaskDialog(supportFragmentManager, object : Callback {
            override fun onSuccess() {
                view.showSnackBar(context.getString(R.string.task_create_successfully))
                view.notifyFragmentToReloadTasks()
            }

            override fun onFailure(message: String) {
                view.showSnackBar(context.getString(R.string.task_create_error))
            }

            override fun onComplete() {

            }
        }, coroutineScope)
            .show(supportFragmentManager, "")
    }
}
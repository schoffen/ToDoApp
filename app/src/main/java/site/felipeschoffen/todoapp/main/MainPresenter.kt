package site.felipeschoffen.todoapp.main

import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource

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
}
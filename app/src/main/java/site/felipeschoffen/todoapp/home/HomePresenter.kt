package site.felipeschoffen.todoapp.home

import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Task

class HomePresenter(override val view: Home.View) : Home.Presenter {
    override fun getTodayTasks() {
        DataSource.getTodayTasks(object : Home.Callback {
            override fun onSuccess(tasks: List<Task>) {
                view.showProgress(false)
                view.displayTodayTasks(tasks)
            }

            override fun onFailure() {
                view.showProgress(false)
                view.displayEmptyTasks()
            }
        })
    }
}
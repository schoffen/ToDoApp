package site.felipeschoffen.todoapp.home

import site.felipeschoffen.todoapp.common.datas.UserTask

interface Home {
    interface View {
        fun showProgress(show: Boolean)
        fun displayTodayTasks(userTasks: List<UserTask>)
        fun displayEmptyTasks()
    }

    interface Presenter {
        val view: Home.View

        fun getTodayTasks()
    }

    interface Callback {
        fun onSuccess(userTasks: List<UserTask>)
        fun onFailure()
    }
}
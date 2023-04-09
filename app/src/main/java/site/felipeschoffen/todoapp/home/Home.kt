package site.felipeschoffen.todoapp.home

import site.felipeschoffen.todoapp.common.datas.Task

interface Home {
    interface View {
        fun showProgress(show: Boolean)
        fun displayTodayTasks(tasks: List<Task>)
        fun displayEmptyTasks()
    }

    interface Presenter {
        val view: Home.View

        fun getTodayTasks()
    }

    interface Callback {
        fun onSuccess(tasks: List<Task>)
        fun onFailure()
    }
}
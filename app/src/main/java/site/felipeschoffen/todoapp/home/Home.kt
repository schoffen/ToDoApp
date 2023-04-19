package site.felipeschoffen.todoapp.home

import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask

interface Home {
    interface View {
        fun showProgress(show: Boolean)
        fun showSnackbar(message: String)
        fun displayTodayTasks(userTasks: List<UserTask>)
        fun displayEmptyTasks()
        fun reloadTasks()
    }

    interface Presenter {
        val view: View

        fun getTodayTasks()
        fun updateTaskStatus(taskUID: String, taskStatus: TaskStatus)
        fun deleteTask(taskUID: String)
    }
}
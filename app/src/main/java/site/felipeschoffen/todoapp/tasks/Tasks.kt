package site.felipeschoffen.todoapp.tasks

import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.datas.TaskStatus

interface Tasks {
    interface View {
        fun showProgress(show: Boolean)
        fun showSnackbar(message: String)
        fun displayEmptyTasks()
        fun displayTasks(tasks: List<TasksByHour>)
        fun getCurrentDate(): SelectedDate
        fun reloadTasks()
    }

    interface Presenter {
        val view: View

        fun getSelectedTasks(selectedDate: SelectedDate)
        fun filterTasksStartWith(prefix: String?)
        fun deleteTask(taskUID: String)
        fun cancelTask(taskUID: String)
        fun updateTaskStatus(taskUID: String, taskStatus: TaskStatus)
    }
}
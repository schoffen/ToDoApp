package site.felipeschoffen.todoapp.tasks

import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.datas.UserTask

interface Tasks {
    interface View {
        fun showProgress(show: Boolean)
        fun displayEmptyTasks()
        fun displayTasks(tasks: List<TasksByHour>)
        fun getCurrentDate(): SelectedDate
    }

    interface Presenter {
        val view: View

        fun getSelectedTasks(selectedDate: SelectedDate)
        fun filterTasksStartWith(prefix: String?)
    }
}
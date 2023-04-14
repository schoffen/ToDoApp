package site.felipeschoffen.todoapp.tasks

import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.UserTask
import java.util.*

class TasksPresenter(override val view: Tasks.View) : Tasks.Presenter {

    override fun getSelectedTasks(selectedDate: SelectedDate) {
        view.showProgress(true)

        DataSource.getTasksByDate(selectedDate) { userTaskList ->
            if (userTaskList.isEmpty()) {
                view.showProgress(false)
                view.displayEmptyTasks()
            } else {
                view.showProgress(false)
                view.displayTasks(sortTaskByHour(userTaskList))
            }
        }
    }

    private fun sortTaskByHour(taskList: List<UserTask>): List<TasksByHour> {
        return taskList.groupBy { task -> task.timestamp.toDate().hours }
            .map { (hour, tasks) -> TasksByHour(hour, tasks.toMutableList()) }
            .sortedBy { tasksByHour -> tasksByHour.hour }
    }
}
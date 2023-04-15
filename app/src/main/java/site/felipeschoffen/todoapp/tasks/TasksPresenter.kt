package site.felipeschoffen.todoapp.tasks

import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.UserTask
import java.util.*

class TasksPresenter(override val view: Tasks.View) : Tasks.Presenter {

    var currentUserTasks: List<UserTask> = emptyList()

    override fun getSelectedTasks(selectedDate: SelectedDate) {
        view.showProgress(true)

        DataSource.getTasksByDate(selectedDate) { userTaskList ->
            if (userTaskList.isEmpty()) {
                currentUserTasks = emptyList()
                view.showProgress(false)
                view.displayEmptyTasks()
            } else {
                currentUserTasks = userTaskList
                view.showProgress(false)
                view.displayTasks(sortTaskByHour(userTaskList))
            }
        }
    }

    override fun filterTasksStartWith(prefix: String?) {
        if (prefix != null) {
            val filteredTasks = filterTasksWithPrefix(prefix, currentUserTasks)
            val sortedFilteredTasks = sortTaskByHour(filteredTasks)
            view.displayTasks(sortedFilteredTasks)
        } else {
            view.displayEmptyTasks()
        }
    }

    private fun filterTasksWithPrefix(prefix: String, taskList: List<UserTask>): List<UserTask> {
        return taskList.filter { it.name.lowercase().startsWith(prefix.lowercase()) }
    }

    private fun sortTaskByHour(taskList: List<UserTask>): List<TasksByHour> {
        return taskList.groupBy { task -> task.timestamp.toDate().hours }
            .map { (hour, tasks) -> TasksByHour(hour, tasks.toMutableList()) }
            .sortedBy { tasksByHour -> tasksByHour.hour }
    }


}
package site.felipeschoffen.todoapp.tasks

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask

class TasksPresenter(override val view: Tasks.View, private val coroutineScope: CoroutineScope) :
    Tasks.Presenter {

    var currentUserTasks: List<UserTask> = emptyList()

    override fun getSelectedTasks(selectedDate: SelectedDate) {
        view.showProgress(true)

        coroutineScope.launch {
            val userTasksList = DataSource.getTasksByDate(selectedDate)

            if (userTasksList.isNotEmpty()) {
                Log.d("scope", "lista cheia")
                currentUserTasks = userTasksList
                view.showProgress(false)
                view.displayTasks(sortTaskByHour(userTasksList))
            } else {
                Log.d("scope", "lista vazia")
                currentUserTasks = emptyList()
                view.showProgress(false)
                view.displayEmptyTasks()
            }
        }
    }

    override fun deleteTask(taskUID: String) {
        coroutineScope.launch {
            val deleted = DataSource.deleteTask(taskUID)
            if (deleted) {
                view.showSnackbar("Tarefa deletada")
                view.reloadTasks()
            }
            else {
                view.showSnackbar("Falha ao deletar")
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

    override fun updateTaskStatus(taskUID: String, taskStatus: TaskStatus) {
        coroutineScope.launch {
            val updated = DataSource.updateTaskStatus(taskUID, taskStatus)
            if (updated)
                view.reloadTasks()
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
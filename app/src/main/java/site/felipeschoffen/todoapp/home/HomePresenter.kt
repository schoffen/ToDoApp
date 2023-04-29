package site.felipeschoffen.todoapp.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.TaskStatus

class HomePresenter(override val view: Home.View, private val coroutineScope: CoroutineScope) : Home.Presenter {
    override fun getTodayTasks() {
        view.showProgress(true)

        coroutineScope.launch {
            val userTasksList = DataSource.getTasksByDate(
                SelectedDate(
                    DateTimeUtils.todayDay,
                    DateTimeUtils.todayMonth,
                    DateTimeUtils.todayYear
                )
            )

            if (userTasksList.isNotEmpty()) {
                view.displayTodayTasks(userTasksList)
                view.showProgress(false)
            } else {
                view.displayEmptyTasks()
                view.showProgress(false)
            }
        }
    }

    override fun updateTaskStatus(taskUID: String, taskStatus: TaskStatus) {
        coroutineScope.launch {
            val updated = DataSource.updateTaskStatus(taskUID, taskStatus)
            if (updated)
                view.reloadTasks()
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
}
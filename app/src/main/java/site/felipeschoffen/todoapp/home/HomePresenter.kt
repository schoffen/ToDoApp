package site.felipeschoffen.todoapp.home

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.CustomDate
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource

class HomePresenter(override val view: Home.View) : Home.Presenter {
    override fun getTodayTasks() {
        DataSource.getTasksByDate(
            SelectedDate(
                CustomDate.todayDay,
                CustomDate.todayMonth,
                CustomDate.todayYear
            )
        ) { taskList ->
            if (taskList.isNotEmpty()) {
                view.displayTodayTasks(taskList)
                view.showProgress(false)
            } else {
                view.displayEmptyTasks()
                view.showProgress(false)
            }
        }
    }
}
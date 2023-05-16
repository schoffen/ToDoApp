package site.felipeschoffen.todoapp.folderTasks

import android.util.Log
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.common.user.UserInformation
import site.felipeschoffen.todoapp.common.util.DateTimeUtils

class FolderTasksPresenter(private val view: FolderTasks.View) : FolderTasks.Presenter {

    lateinit var folder: Folder
    private var isStatusFolder: Boolean = true
    private var month: Int = DateTimeUtils.todayMonth

    override fun onDateChanged(month: Int, year: Int) {

    }

    override fun setupViewByFolder(folderID: String) {
        getFolderByID(folderID)

        val title = folder.name
        view.changeTitleText(title)
        view.changeMonthAndYear(DateTimeUtils.todayMonth, DateTimeUtils.todayYear)

        if (isStatusFolder)
            view.displayUserTasks(userTaskToUserTaskByDate(getFolderTasksByStatus()))
        else
            view.displayUserTasks(userTaskToUserTaskByDate(getFolderTasksByID()))

    }

    private fun getFolderByID(folderID: String) {
        folder = when (folderID) {
            Constants.COMPLETED_FOLDER.uid -> Constants.COMPLETED_FOLDER
            Constants.CANCELED_FOLDER.uid -> Constants.CANCELED_FOLDER
            Constants.ON_GOING_FOLDER.uid -> Constants.ON_GOING_FOLDER
            Constants.PENDING_FOLDER.uid -> Constants.PENDING_FOLDER
            else -> {
                DataSource.getFolderInfo(folderID).also { isStatusFolder = true }
            }
        }
    }

    private fun getFolderTasksByID(): List<UserTask> {
        return UserInformation.getUserTasks().filter { it.folder?.uid == folder.uid }
            .filter { it.timestamp.toDate().month == month }
    }

    private fun getFolderTasksByStatus(): List<UserTask> {
        return UserInformation.getUserTasks().filter { it.status.name == folder.uid}
            .filter { it.timestamp.toDate().month == month }
    }

    private fun userTaskToUserTaskByDate(userTaskList: List<UserTask>): List<UserTaskByDate> {
        return userTaskList.groupBy { task -> task.timestamp }
            .map { (day, tasks) -> UserTaskByDate(day, tasks.toMutableList()) }
            .sortedBy { userTaskByDate -> userTaskByDate.timestamp.toDate().day }
    }
}
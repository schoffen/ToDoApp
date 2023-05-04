package site.felipeschoffen.todoapp.folderTasks

import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Folder

class FolderTasksPresenter(private val view: FolderTasks.View) : FolderTasks.Presenter {

    lateinit var folder: Folder

    override fun onDateChanged(month: Int, year: Int) {

    }

    override fun setupViewByFolder(folderID: String) {
        getFolderByID(folderID)

        val title = folder.name
        view.changeTitleText(title)
    }

    private fun getFolderByID(folderID: String) {
        when (folderID) {
            Constants.COMPLETED_FOLDER.uid -> folder = Constants.COMPLETED_FOLDER
            Constants.CANCELED_FOLDER.uid -> folder = Constants.CANCELED_FOLDER
            Constants.ON_GOING_FOLDER.uid -> folder = Constants.ON_GOING_FOLDER
            Constants.PENDING_FOLDER.uid -> folder = Constants.PENDING_FOLDER
            else -> {
                // data base get folder
            }
        }
    }
}
package site.felipeschoffen.todoapp.folderTasks

import site.felipeschoffen.todoapp.common.datas.Folder

interface FolderTasks {
    interface View {
        fun changeMonthAndYear(month: Int, year: Int)
        fun changeTitleText(title: String)
        fun displayUserTasks(userTasksByDateList: List<UserTaskByDate>)
        fun displayEmptyTasks()
    }

    interface Presenter {
        fun onDateChanged(month: Int, year: Int)
        fun setupViewByFolder(folderID: String)
    }
}
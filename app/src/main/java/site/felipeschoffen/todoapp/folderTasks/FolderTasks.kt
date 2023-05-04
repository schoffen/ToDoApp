package site.felipeschoffen.todoapp.folderTasks

import site.felipeschoffen.todoapp.common.datas.Folder

interface FolderTasks {
    interface View {
        fun changeTitleText(title: String)
    }

    interface Presenter {
        fun onDateChanged(month: Int, year: Int)
        fun setupViewByFolder(folderID: String)
    }
}
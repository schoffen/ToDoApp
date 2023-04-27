package site.felipeschoffen.todoapp.common

import site.felipeschoffen.todoapp.common.datas.Folder

object Constants {
    const val PASSWORD_MIN_LENGTH = 8
    const val NAME_MIN_LENGTH = 3

    const val colorGreen = "green"
    const val colorOrange = "orange"
    const val colorRed = "red"
    const val colorPurple = "purple"

    val createNewFolderReference = Folder("newFolderButton", "newFolderButton", colorOrange)
}
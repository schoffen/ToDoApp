package site.felipeschoffen.todoapp.common

import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.TaskStatus

object Constants {
    const val PASSWORD_MIN_LENGTH = 8
    const val NAME_MIN_LENGTH = 3

    const val COLOR_GREEN = "green"
    const val COLOR_ORANGE = "orange"
    const val COLOR_RED = "red"
    const val COLOR_PURPLE = "purple"

    const val EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID"

    val CREATE_NEW_FOLDER_REFERENCE = Folder("newFolderButton", "newFolderButton", COLOR_ORANGE)
    val NONE_FOLDER_SELECTED_REFERENCE = Folder("none", "(Nenhum)", COLOR_ORANGE)

    val PENDING_FOLDER = Folder(TaskStatus.PENDING.toString(), "Pendentes", "")
    val CANCELED_FOLDER = Folder(TaskStatus.CANCELED.toString(), "Canceladas", "")
    val COMPLETED_FOLDER = Folder(TaskStatus.COMPLETED.toString(), "Completas", "")
    val ON_GOING_FOLDER = Folder(TaskStatus.ON_GOING.toString(), "Em andamento", "")
}
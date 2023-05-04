package site.felipeschoffen.todoapp.common.user

import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.UserTask

data class UserInfo(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var foldersList: MutableList<Folder> = mutableListOf(),
    var userTasksList: MutableList<UserTask> = mutableListOf()
)

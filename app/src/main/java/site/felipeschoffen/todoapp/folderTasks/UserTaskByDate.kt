package site.felipeschoffen.todoapp.folderTasks

import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.datas.UserTask

data class UserTaskByDate(
    var timestamp: Timestamp,
    var userTasksList: MutableList<UserTask>
)

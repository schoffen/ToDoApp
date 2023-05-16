package site.felipeschoffen.todoapp.common.user

import android.util.Log
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask

object UserInformation {

    private lateinit var userInfo: UserInfo

    fun addFolderToFoldersList(folder: Folder) {
        userInfo.foldersList.add(folder)
    }

    fun getFoldersList(): List<Folder> {
        return userInfo.foldersList
    }

    fun getUserTasks(): List<UserTask> {
        return userInfo.userTasksList
    }

    fun setUserInfo(userInfo: UserInfo) {
        this.userInfo = userInfo
        Log.d("User", userInfo.userTasksList.toString())
    }

    fun getUserInfo(): UserInfo {
        return userInfo
    }

    fun addNewUserTask(userTask: UserTask) {
        userInfo.userTasksList.add(userTask)
    }

    fun deleteUserTaskByID(userTaskID: String) {
        userInfo.userTasksList.removeIf { it.uid == userTaskID }
    }

    fun editTask(userTask: UserTask) {
        userInfo.userTasksList.find { it.uid == userTask.uid }?.apply {
            name = userTask.name
            status = userTask.status
            timestamp = userTask.timestamp
            priorityTag = userTask.priorityTag
            folder = userTask.folder
        }
    }

    fun updateUserTaskStatusByID(userTaskID: String, status: TaskStatus) {
        userInfo.userTasksList.find { it.uid == userTaskID }.let {
            it?.status = status
        }
    }

    fun deleteFolderAndTasksInFolderByID(folderID: String) {
        userInfo.foldersList.removeIf { it.uid == folderID }
        userInfo.userTasksList.removeIf { it.folder?.uid == folderID }
    }
}


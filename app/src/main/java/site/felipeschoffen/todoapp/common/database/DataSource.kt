package site.felipeschoffen.todoapp.common.database

import android.icu.util.Calendar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.datas.*
import site.felipeschoffen.todoapp.common.user.UserInfo
import site.felipeschoffen.todoapp.common.user.UserInformation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DataSource {

    private var currentUser = FirebaseAuth.getInstance().currentUser
    var userInfo = UserInfo()

    suspend fun login(
        email: String,
        password: String,
    ): LoginResult {
        return try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()

            if (setUserInfo())
                LoginResult(true, null)

            LoginResult(true, null)
        } catch (e: Exception) {
            val error = when (e) {
                is FirebaseAuthInvalidCredentialsException -> DatabaseError.INVALID_CREDENTIALS
                else -> null
            }

            LoginResult(false, error)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): RegisterResult {
        return try {
            val userCreated =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            currentUser = userCreated.user

            val user = UserInfo(
                currentUser!!.uid,
                name,
                email
            )

            FirebaseFirestore.getInstance().collection("/users")
                .document(user.uid)
                .set(user).await()

            if (setUserInfo())
                RegisterResult(true, null)
            else
                RegisterResult(false, null)
        } catch (e: Exception) {
            RegisterResult(false, null)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
    }

    suspend fun getSession(): Boolean {
        return if (FirebaseAuth.getInstance().currentUser != null) {
            currentUser = FirebaseAuth.getInstance().currentUser

            setUserInfo()
            true
        } else {
            false
        }
    }

    private suspend fun getUserInfo(): UserInfo {
        val userInfo = UserInfo()

        FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).get().addOnSuccessListener {
                userInfo.uid = it.data?.get("uid").toString()
                userInfo.name = it.data?.get("name").toString()
                userInfo.email = currentUser!!.email.toString()
            }.await()

        userInfo.foldersList.addAll(getFirebaseCurrentUserFolders())
        userInfo.userTasksList.addAll(getFirebaseCurrentUserTasks())

        return userInfo
    }

    private suspend fun getFirebaseCurrentUserTasks(): List<UserTask> {
        val userTasksList = mutableListOf<UserTask>()

        FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).collection("tasks").get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    documents.forEach { document ->
                        userTasksList.add(document.toObject(UserTask::class.java))
                    }
                }
            }.await()

        return userTasksList
    }

    private suspend fun getFirebaseCurrentUserFolders(): List<Folder> {
        val foldersList = mutableListOf<Folder>()

        FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).collection("folders").get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    documents.forEach { document ->
                        foldersList.add(document.toObject(Folder::class.java))
                    }
                }
            }.await()

        return foldersList
    }

    private suspend fun setUserInfo(): Boolean {
        val userInfo = getUserInfo()

        return run {
            UserInformation.setUserInfo(userInfo)
            true
        }
    }

    suspend fun createTask(userTask: UserTask): Boolean = suspendCoroutine { continuation ->
        currentUserTasksRef()
            .document(userTask.uid).set(userTask)
            .addOnSuccessListener {
                UserInformation.addNewUserTask(userTask)
                continuation.resume(true)
            }
            .addOnFailureListener { continuation.resume(false) }
    }

    fun getTasksByDate(selectedDate: SelectedDate): List<UserTask> {
        return UserInformation.getUserInfo().userTasksList.filter { userTask ->
            checkFirebaseTimestampEqualsDate(userTask.timestamp, selectedDate)
        }
    }

    suspend fun deleteTask(userTaskID: String): Boolean = suspendCoroutine { continuation ->
        currentUserTasksRef().document(userTaskID).delete()
            .addOnSuccessListener {
                UserInformation.deleteUserTaskByID(userTaskID)
                continuation.resume(true)
            }
            .addOnFailureListener { continuation.resume(false) }
    }

    suspend fun updateTaskStatus(taskUID: String, newTaskStatus: TaskStatus): Boolean =
        suspendCoroutine { continuation ->
            currentUserTasksRef().document(taskUID)
                .update(hashMapOf<String, Any>("status" to newTaskStatus))
                .addOnSuccessListener {
                    UserInformation.updateUserTaskStatusByID(taskUID, newTaskStatus)
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }

    suspend fun editTask(userTask: UserTask): Boolean = suspendCoroutine { continuation ->
        currentUserTasksRef().document(userTask.uid).set(userTask)
            .addOnSuccessListener {
                UserInformation.editTask(userTask)
                continuation.resume(true)
            }
            .addOnFailureListener { continuation.resume(false) }
    }

    private fun checkFirebaseTimestampEqualsDate(
        timestamp: Timestamp,
        selectedDate: SelectedDate
    ): Boolean {
        val date = timestamp.toDate()

        val calendar = Calendar.getInstance()
        calendar.time = date

        val isYearEqual =
            calendar.get(Calendar.YEAR) == selectedDate.year
        val isMonthEqual =
            calendar.get(Calendar.MONTH) == selectedDate.month
        val isDayEqual =
            calendar.get(Calendar.DAY_OF_MONTH) == selectedDate.day

        return isDayEqual && isMonthEqual && isYearEqual
    }

    private fun currentUserTasksRef(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).collection("tasks")
    }

    private fun currentUserFoldersRef(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).collection("folders")
    }

    suspend fun addFolder(folder: Folder): Boolean = suspendCoroutine { continuation ->
        currentUserFoldersRef().document(folder.uid).set(folder)
            .addOnSuccessListener {
                UserInformation.addFolderToFoldersList(folder)
                continuation.resume(true)
            }
            .addOnFailureListener { continuation.resume(false) }
    }

    fun getFolders(): List<Folder> {
        return UserInformation.getFoldersList()
    }

    suspend fun deleteFolderAndTasksInFolder(folder: Folder): Boolean = suspendCoroutine { continuation ->
        currentUserFoldersRef().document(folder.uid).delete()
            .addOnSuccessListener {
                currentUserTasksRef().get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val task = document.toObject<UserTask>()
                            if (task.folder!!.uid == folder.uid) {
                                document.reference.delete()
                            }
                        }

                        UserInformation.deleteFolderAndTasksInFolderByID(folder.uid)

                        continuation.resume(true)
                    }
            }
            .addOnFailureListener { continuation.resume(false) }
    }

    fun getFolderInfo(folderID: String): Folder {
        return UserInformation.getFoldersList().first { it.uid == folderID }
    }

    fun getTasksByFolder(folder: Folder): List<UserTask> {
        return UserInformation.getUserTasks().filter { it.folder?.uid == folder.uid }
    }
}
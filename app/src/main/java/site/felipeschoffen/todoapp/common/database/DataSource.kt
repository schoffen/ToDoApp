package site.felipeschoffen.todoapp.common.database

import android.icu.util.Calendar
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserInfo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DataSource {

    var currentUser = FirebaseAuth.getInstance().currentUser
    var userInfo = UserInfo("", "", "")

    suspend fun login(
        email: String,
        password: String,
        coroutineScope: CoroutineScope
    ): LoginResult = suspendCoroutine { continuation ->
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                currentUser = FirebaseAuth.getInstance().currentUser

                coroutineScope.launch {
                    if (setUserInfo())
                        continuation.resume(LoginResult(true, null))
                    else
                        continuation.resume(
                            LoginResult(
                                false,
                                DatabaseError.FAILED_TO_SET_USER_INFO
                            )
                        )
                }
            }
            .addOnFailureListener {
                try {
                    throw it
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    continuation.resume(LoginResult(false, DatabaseError.INVALID_CREDENTIALS))
                } catch (e: FirebaseException) {
                    Log.d("FirebaseException", e.message.toString())
                }
            }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        coroutineScope: CoroutineScope
    ): RegisterResult = suspendCoroutine { continuation ->
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                currentUser = FirebaseAuth.getInstance().currentUser

                val user = hashMapOf(
                    "name" to name,
                    "uid" to currentUser?.uid
                )

                FirebaseFirestore.getInstance().collection("/users")
                    .document(currentUser?.uid.toString())
                    .set(user)
                    .addOnSuccessListener {
                        coroutineScope.launch {
                            val isUserInfoSet = setUserInfo()
                            if (isUserInfoSet)
                                continuation.resume(RegisterResult(true, null))
                            else
                                continuation.resume(RegisterResult(false, null))
                        }
                    }
            }
            .addOnFailureListener {
                continuation.resume(RegisterResult(false, null))
                // tratar essa exception
            }
    }
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
    }

    suspend fun getSession(): Boolean = coroutineScope {
        if (FirebaseAuth.getInstance().currentUser != null) {
            currentUser = FirebaseAuth.getInstance().currentUser

            return@coroutineScope setUserInfo()
        } else {
            return@coroutineScope false
        }
    }

    private suspend fun setUserInfo(): Boolean = suspendCoroutine { continuation ->
        val uid = currentUser?.uid.toString()
        val email = currentUser?.email.toString()
        var name = ""

        FirebaseFirestore.getInstance().collection("/users").document(uid).get()
            .addOnSuccessListener {
                if (it != null) {
                    name = it.data?.get("name").toString()
                    userInfo.uid = uid
                    userInfo.email = email
                    userInfo.name = name
                    continuation.resume(true)
                } else
                    continuation.resume(false)
            }
    }

    suspend fun createTag(tag: Tag): Boolean = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance().collection("/users").document(currentUser!!.uid)
            .collection("tags")
            .document(tag.name).set(tag)
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener { continuation.resume(false) }
    }

    fun getUserTags(callback: (List<Tag>) -> Unit) {
        val tags = mutableListOf<Tag>()

        FirebaseFirestore.getInstance().collection("/users").document(currentUser!!.uid)
            .collection("tags").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val tag =
                        Tag(
                            document.data["uid"].toString(),
                            document.data["name"].toString(),
                            document.data["color"].toString()
                        )
                    tags.add(tag)
                }

                callback(tags)
            }
    }

    fun createTask(userTask: UserTask, callback: Callback) {
        currentUserTasksRef()
            .document(userTask.uid).set(userTask)
            .addOnSuccessListener { callback.onSuccess() }
            .addOnFailureListener { callback.onFailure("Não foi possível adicionar tag") }
            .addOnCompleteListener { callback.onComplete() }
    }

    suspend fun getTasksByDate(selectedDate: SelectedDate): List<UserTask> =
        suspendCoroutine { continuation ->
            val userTaskList = mutableListOf<UserTask>()

            currentUserTasksRef().get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        documents.forEach { document ->
                            document.getTimestamp("timestamp")?.run {
                                if (checkFirebaseTimestampEqualsDate(this, selectedDate)) {
                                    userTaskList.add(queryDocumentSnapShotToUserTask(document))
                                }
                            }
                        }
                    }
                    continuation.resume(userTaskList)
                }
                .addOnFailureListener { continuation.resume(userTaskList) }
        }

    suspend fun deleteTask(taskUID: String): Boolean = suspendCoroutine { continuation ->
        currentUserTasksRef().document(taskUID).delete()
            .addOnSuccessListener { continuation.resume(true) }
            .addOnFailureListener { continuation.resume(false) }
    }

    suspend fun updateTaskStatus(taskUID: String, taskStatus: TaskStatus): Boolean =
        suspendCoroutine { continuation ->
            currentUserTasksRef().document(taskUID).update(
                hashMapOf<String, Any>("status" to taskStatus)
            ).addOnSuccessListener {
                continuation.resume(true)
            }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }

    private fun queryDocumentSnapShotToUserTask(document: QueryDocumentSnapshot): UserTask {
        val taskUid = document.data["uid"].toString()
        val taskName = document.data["name"].toString()
        val taskTimestamp = document.data["timestamp"]
        val taskTags = hashTagsToTagList(document.data["tags"] as List<*>)
        val taskStatus =
            stringToTaskStatus(document.data["status"].toString())

        return UserTask(
            taskUid,
            taskName,
            taskTimestamp as Timestamp,
            taskTags,
            taskStatus as TaskStatus
        )
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

    private fun hashTagsToTagList(tagsListRef: List<*>): List<Tag> {
        val taskTags = mutableListOf<Tag>()

        for (tag in tagsListRef) {
            val tagHash = tag as HashMap<*, *>
            taskTags.add(
                Tag(
                    tagHash["uid"].toString(),
                    tagHash["name"].toString(),
                    tagHash["color"].toString()
                )
            )
        }

        return taskTags
    }

    private fun stringToTaskStatus(data: String): TaskStatus? {
        return when (data) {
            TaskStatus.COMPLETED.toString() -> TaskStatus.COMPLETED
            TaskStatus.CANCELED.toString() -> TaskStatus.CANCELED
            TaskStatus.ON_GOING.toString() -> TaskStatus.ON_GOING
            TaskStatus.PENDING.toString() -> TaskStatus.PENDING
            else -> null
        }
    }

    private fun currentUserTasksRef(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("/users")
            .document(currentUser!!.uid).collection("tasks")
    }
}
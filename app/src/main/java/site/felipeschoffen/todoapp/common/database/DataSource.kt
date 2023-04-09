package site.felipeschoffen.todoapp.common.database

import android.icu.util.Calendar
import android.text.style.TabStopSpan
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.CustomDate
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.common.datas.Task
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserInfo
import site.felipeschoffen.todoapp.home.Home

object DataSource {

    var currentUser = FirebaseAuth.getInstance().currentUser
    var userInfo = UserInfo("", "", "")

    fun login(email: String, password: String, callback: DatabaseCallback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                currentUser = FirebaseAuth.getInstance().currentUser
                setUserInfo(object : Callback {
                    override fun onSuccess() {
                        callback.onSuccess()
                    }

                    override fun onFailure(message: String) {
                    }

                    override fun onComplete() {
                    }
                })
            }
            .addOnFailureListener {
                try {
                    throw it
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    callback.onFailure(DatabaseError.INVALID_CREDENTIALS)
                } catch (e: FirebaseException) {
                    Log.d("FirebaseException", e.message.toString())
                }
            }
            .addOnCompleteListener {

                callback.onComplete()
            }
    }

    fun register(name: String, email: String, password: String, callback: Callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                currentUser = FirebaseAuth.getInstance().currentUser
                setUserInfo(object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onFailure(message: String) {
                    }

                    override fun onComplete() {
                    }
                })

                val user = hashMapOf(
                    "name" to name,
                    "uid" to currentUser?.uid
                )

                FirebaseFirestore.getInstance().collection("/users")
                    .document(currentUser?.uid.toString())
                    .set(user)

                callback.onSuccess()
            }
            .addOnFailureListener { exception -> callback.onFailure(exception.message.toString()) }
            .addOnCompleteListener {

                callback.onComplete()
            }
    }

    fun logout(callback: Callback) {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
        callback.onSuccess()
        callback.onComplete()
    }

    fun getSession(): Boolean {
        return if (FirebaseAuth.getInstance().currentUser != null) {
            currentUser = FirebaseAuth.getInstance().currentUser
            true
        } else {
            false
        }
    }

    fun setUserInfo(callback: Callback?) {
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
                    Log.d("name", name)
                    callback?.onSuccess()
                    callback?.onComplete()
                } else
                    callback?.onFailure("Falhou ao alocar nome")
            }
    }

    fun createTag(tag: Tag, callback: Callback) {
        FirebaseFirestore.getInstance().collection("/users").document(currentUser!!.uid)
            .collection("tags")
            .document(tag.name).set(tag)
            .addOnSuccessListener { callback.onSuccess() }
            .addOnFailureListener { callback.onFailure("Não foi possível adicionar tag") }
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

    fun createTask(task: Task, callback: Callback) {
        FirebaseFirestore.getInstance().collection("/users").document(currentUser!!.uid)
            .collection("tasks")
            .document(task.uid).set(task)
            .addOnSuccessListener { callback.onSuccess() }
            .addOnFailureListener { callback.onFailure("Não foi possível adicionar tag") }
            .addOnCompleteListener { callback.onComplete() }
    }

    fun getTodayTasks(callback: Home.Callback) {
        val tasks = mutableListOf<Task>()

        FirebaseFirestore.getInstance().collection("/users").document(currentUser!!.uid)
            .collection("tasks").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    callback.onFailure()
                    return@addOnSuccessListener
                } else {
                    for (task in documents) {
                        val date = task.getTimestamp("timestamp")?.toDate()

                        if (date != null) {
                            val taskCalendar = Calendar.getInstance()
                            taskCalendar.time = date

                            val isYearEqual =
                                taskCalendar.get(Calendar.YEAR) == CustomDate.todayYear
                            val isMonthEqual =
                                taskCalendar.get(Calendar.MONTH) == CustomDate.todayMonth
                            val isDayEqual =
                                taskCalendar.get(Calendar.DAY_OF_MONTH) == CustomDate.todayDay

                            if (isYearEqual && isMonthEqual && isDayEqual) {
                                FirebaseFirestore.getInstance().collection("/users")
                                    .document(currentUser!!.uid)
                                    .collection("tasks").document(task.data["uid"].toString()).get()
                                    .addOnSuccessListener {

                                        if (it != null) {
                                            val taskUid = it.data?.get("uid").toString()
                                            val taskName = it.data?.get("name").toString()
                                            val taskTimestamp = it.data?.get("timestamp")

                                            val tagsListRef = it.data?.get("tags") as List<*>
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

                                            val taskStatus =
                                                when (it.data?.get("status").toString()) {
                                                    TaskStatus.COMPLETED.toString() -> TaskStatus.COMPLETED
                                                    TaskStatus.CANCELED.toString() -> TaskStatus.CANCELED
                                                    TaskStatus.ON_GOING.toString() -> TaskStatus.ON_GOING
                                                    TaskStatus.PENDING.toString() -> TaskStatus.PENDING
                                                    else -> ""
                                                }

                                            tasks.add(
                                                Task(
                                                    taskUid,
                                                    taskName,
                                                    taskTimestamp as Timestamp,
                                                    taskTags,
                                                    taskStatus as TaskStatus
                                                )
                                            )

                                            callback.onSuccess(tasks)
                                        }
                                    }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                callback.onFailure()
            }
    }
}
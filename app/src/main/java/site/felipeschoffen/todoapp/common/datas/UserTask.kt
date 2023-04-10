package site.felipeschoffen.todoapp.common.datas

import com.google.firebase.Timestamp


data class UserTask (
    val uid: String,
    val name: String,
    val timestamp: Timestamp,
    val tags: List<Tag>,
    val status: TaskStatus
        )
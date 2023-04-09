package site.felipeschoffen.todoapp.common.datas

import com.google.firebase.Timestamp


data class Task (
    val uid: String,
    val name: String,
    val timestamp: Timestamp,
    val tags: List<Tag>,
    val status: TaskStatus
        )
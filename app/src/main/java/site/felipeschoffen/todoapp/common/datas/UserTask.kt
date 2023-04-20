package site.felipeschoffen.todoapp.common.datas

import com.google.firebase.Timestamp


data class UserTask (
    var uid: String = "",
    var name: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var tags: List<Tag> = emptyList(),
    var status: TaskStatus = TaskStatus.PENDING
        )
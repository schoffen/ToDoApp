package site.felipeschoffen.todoapp.common.datas

import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.util.PriorityTag

data class UserTask (
    var uid: String = "",
    var name: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var priorityTag: PriorityTag = PriorityTag.LOW_PRIORITY,
    var status: TaskStatus = TaskStatus.PENDING,
    var folder: Folder? = null
        )
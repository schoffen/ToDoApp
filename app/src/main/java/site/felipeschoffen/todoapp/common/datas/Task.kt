package site.felipeschoffen.todoapp.common.datas

import java.sql.Timestamp

data class Task (
    val uid: String,
    val name: String,
    val timestamp: Timestamp,
    val tags: List<Tag>
        )
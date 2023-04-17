package site.felipeschoffen.todoapp.common.adapters

import site.felipeschoffen.todoapp.common.datas.TaskStatus

interface TaskAdapterListener {
    fun onDeleteTask(taskUID: String)
    fun onCancelTask(taskUID: String)
    fun onUpdateTaskStatus(taskUID: String, taskStatus: TaskStatus)
}
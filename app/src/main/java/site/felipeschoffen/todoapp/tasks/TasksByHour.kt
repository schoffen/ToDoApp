package site.felipeschoffen.todoapp.tasks

import site.felipeschoffen.todoapp.common.datas.UserTask

data class TasksByHour(val hour: Int, val userTasksList: MutableList<UserTask>)

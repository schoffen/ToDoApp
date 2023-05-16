package site.felipeschoffen.todoapp.common.util

interface Callback {
    fun onSuccess()
    fun onFailure(message: String)
    fun onComplete()
}
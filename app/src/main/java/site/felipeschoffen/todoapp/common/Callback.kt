package site.felipeschoffen.todoapp.common

interface Callback {
    fun onSuccess()
    fun onFailure(message: String)
    fun onComplete()
}
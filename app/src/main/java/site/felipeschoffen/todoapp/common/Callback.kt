package site.felipeschoffen.todoapp.common

interface Callback {
    fun onSuccess()
    fun onFailure()
    fun onComplete()
}
package site.felipeschoffen.todoapp.common.database

interface DatabaseCallback {
    fun onSuccess()
    fun onFailure(error: DatabaseError)
    fun onComplete()
}
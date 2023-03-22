package site.felipeschoffen.todoapp.common

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object DataSource {
    fun login(email: String, password: String, callback: Callback) {

    }

    fun register(email: String, password: String, callback: Callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { callback.onSuccess() }
            .addOnFailureListener { exception ->
                callback.onFailure(exception.message.toString())
            }
            .addOnCompleteListener { callback.onComplete() }
    }
}
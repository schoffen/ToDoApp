package site.felipeschoffen.todoapp.register

import android.util.Log
import android.util.Patterns

class RegisterPresenter(val view: Register.View?) : Register.Presenter {
    override fun register(email: String, password: String, confirmPassword: String) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.d("email", "valido")
        } else {
            view?.displayEmailError(RegisterErrors.EMAIL_INVALID)
        }
    }
}
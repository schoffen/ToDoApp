package site.felipeschoffen.todoapp.common

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class CustomTextWatcher(private val customTextChanged: (s: CharSequence?) -> Unit) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        customTextChanged(s)
    }

    override fun afterTextChanged(p0: Editable?) {

    }
}
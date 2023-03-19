package site.felipeschoffen.todoapp.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), Register.View {
    private val presenter: RegisterPresenter = RegisterPresenter(this)

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerRegisterBtn.setOnClickListener {
            val email = binding.registerEmailET.text.toString()
            val password = binding.registerPasswordET.text.toString()
            val confirmPassword = binding.registerConfirmPasswordET.text.toString()

            presenter.register(email, password, confirmPassword)
        }
    }

    override fun displayEmailError(error: RegisterErrors) {
        if (error == RegisterErrors.EMAIL_ALREADY_IN_USE) {
            binding.registerEmailTIL.error = "Email já cadastrado"
        } else if (error == RegisterErrors.EMAIL_INVALID) {
            binding.registerEmailTIL.error = "Formato de email inválido"
        }
    }

    override fun displayPasswordError(error: RegisterErrors) {
        if (error == RegisterErrors.PASSWORD_SHORT_LENGTH) {
            binding.registerPasswordTIL.error = "Senha deve conter 8 caracteres ou mais"
        } else if (error == RegisterErrors.PASSWORD_NOT_MATCH) {
            binding.registerConfirmPasswordTIL.error = "Senhas não conferem"
        }
    }
}
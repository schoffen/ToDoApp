package site.felipeschoffen.todoapp.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.common.CustomTextWatcher
import site.felipeschoffen.todoapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), Register.View {
    private val presenter: RegisterPresenter = RegisterPresenter(this)

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerEmailET.addTextChangedListener(CustomTextWatcher {
            displayEmailError(null)
        })

        binding.registerPasswordET.addTextChangedListener(CustomTextWatcher {
            displayPasswordError(null)
        })

        binding.registerConfirmPasswordET.addTextChangedListener(CustomTextWatcher {
            displayConfirmPasswordError(null)
        })

        binding.registerRegisterBtn.setOnClickListener {
            val email = binding.registerEmailET.text.toString()
            val password = binding.registerPasswordET.text.toString()
            val confirmPassword = binding.registerConfirmPasswordET.text.toString()

            presenter.register(email, password, confirmPassword)
        }
    }

    override fun displayEmailError(error: RegisterErrors?) {
        when (error) {
            RegisterErrors.EMAIL_ALREADY_IN_USE -> {
                binding.registerEmailTIL.error = "Email já cadastrado"
            }
            RegisterErrors.EMAIL_FORMAT_INVALID -> {
                binding.registerEmailTIL.error = "Formato de email inválido"
            }
            RegisterErrors.FIELD_EMPTY -> {
                binding.registerEmailTIL.error = "Email não pode estar vazio"
            }
            else -> {
                binding.registerEmailTIL.error = null
            }
        }
    }

    override fun displayPasswordError(error: RegisterErrors?) {
        when (error) {
            RegisterErrors.PASSWORD_SHORT_LENGTH -> {
                binding.registerPasswordTIL.error = "Senha deve conter 8 caracteres ou mais"
            }
            RegisterErrors.PASSWORD_NOT_MATCH -> {
                binding.registerPasswordTIL.error = "Senhas não conferem"
            }
            RegisterErrors.FIELD_EMPTY -> {
                binding.registerPasswordTIL.error = "Senha não pode estar vazia"
            }
            else -> {
                binding.registerPasswordTIL.error = null
            }
        }
    }

    override fun displayConfirmPasswordError(error: RegisterErrors?) {
        when (error) {
            RegisterErrors.PASSWORD_SHORT_LENGTH -> {
                binding.registerConfirmPasswordTIL.error = "Senha deve conter 8 caracteres ou mais"
            }
            RegisterErrors.PASSWORD_NOT_MATCH -> {
                binding.registerConfirmPasswordTIL.error = "Senhas não conferem"
            }
            RegisterErrors.FIELD_EMPTY -> {
                binding.registerConfirmPasswordTIL.error = "Senha não pode estar vazia"
            }
            else -> {
                binding.registerConfirmPasswordTIL.error = null
            }
        }
    }
}
package site.felipeschoffen.todoapp.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.CustomTextWatcher
import site.felipeschoffen.todoapp.common.InputErrors
import site.felipeschoffen.todoapp.databinding.ActivityRegisterBinding
import site.felipeschoffen.todoapp.login.LoginActivity
import site.felipeschoffen.todoapp.main.MainActivity

class RegisterActivity : AppCompatActivity(), Register.View {
    override val presenter: RegisterPresenter = RegisterPresenter(this)

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            registerNameET.addTextChangedListener(CustomTextWatcher {
                displayNameError(null)
            })

            registerEmailET.addTextChangedListener(CustomTextWatcher {
                displayEmailError(null)
            })

            registerPasswordET.addTextChangedListener(CustomTextWatcher {
                displayPasswordError(null)
            })

            registerConfirmPasswordET.addTextChangedListener(CustomTextWatcher {
                displayConfirmPasswordError(null)
            })
        }

        binding.registerRegisterBtn.setOnClickListener {
            val name = binding.registerNameET.text.toString()
            val email = binding.registerEmailET.text.toString()
            val password = binding.registerPasswordET.text.toString()
            val confirmPassword = binding.registerConfirmPasswordET.text.toString()

            presenter.register(name, email, password, confirmPassword)
        }

        binding.registerLoginTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun displayNameError(error: InputErrors?) {
        when (error) {
            InputErrors.FIELD_EMPTY -> {
                binding.registerNameTIL.error = getString(R.string.empty_field)
            }

            InputErrors.NAME_SHORT_LENGHT -> {
                binding.registerNameTIL.error = getString(R.string.name_short_error)
            }
            else -> {
                binding.registerNameTIL.error = null
            }
        }

    }

    override fun displayEmailError(error: InputErrors?) {
        when (error) {
            InputErrors.EMAIL_ALREADY_IN_USE -> {
                binding.registerEmailTIL.error = getString(R.string.email_in_use)
            }
            InputErrors.EMAIL_FORMAT_INVALID -> {
                binding.registerEmailTIL.error = getString(R.string.invalid_email)
            }
            InputErrors.FIELD_EMPTY -> {
                binding.registerEmailTIL.error = getString(R.string.empty_field)
            }
            else -> {
                binding.registerEmailTIL.error = null
            }
        }
    }

    override fun displayPasswordError(error: InputErrors?) {
        when (error) {
            InputErrors.PASSWORD_SHORT_LENGTH -> {
                binding.registerPasswordTIL.error = getString(R.string.password_short_length)
            }
            InputErrors.PASSWORD_NOT_MATCH -> {
                binding.registerPasswordTIL.error = getString(R.string.password_not_match)
            }
            InputErrors.FIELD_EMPTY -> {
                binding.registerPasswordTIL.error = getString(R.string.empty_field)
            }
            else -> {
                binding.registerPasswordTIL.error = null
            }
        }
    }

    override fun displayConfirmPasswordError(error: InputErrors?) {
        when (error) {
            InputErrors.PASSWORD_SHORT_LENGTH -> {
                binding.registerConfirmPasswordTIL.error = getString(R.string.password_short_length)
            }
            InputErrors.PASSWORD_NOT_MATCH -> {
                binding.registerConfirmPasswordTIL.error = getString(R.string.password_not_match)
            }
            InputErrors.FIELD_EMPTY -> {
                binding.registerConfirmPasswordTIL.error = getString(R.string.empty_field)
            }
            else -> {
                binding.registerConfirmPasswordTIL.error = null
            }
        }
    }

    override fun displayRegisterButtonProgress(show: Boolean) {
        when (show) {
            true -> {
                binding.registerProgressBar.visibility = View.VISIBLE
                binding.registerRegisterBtn.visibility = View.INVISIBLE
            }
            false -> {
                binding.registerProgressBar.visibility = View.INVISIBLE
                binding.registerRegisterBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
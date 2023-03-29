package site.felipeschoffen.todoapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.CustomTextWatcher
import site.felipeschoffen.todoapp.common.InputErrors
import site.felipeschoffen.todoapp.databinding.ActivityLoginBinding
import site.felipeschoffen.todoapp.main.MainActivity
import site.felipeschoffen.todoapp.register.RegisterActivity

class LoginActivity : AppCompatActivity(), Login.View {

    override val presenter: Login.Presenter = LoginPresenter(this)
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginEmailET.addTextChangedListener (CustomTextWatcher {
            displayEmailError(null)
        })

        binding.loginPasswordET.addTextChangedListener (CustomTextWatcher {
            displayPasswordError(null)
        })

        binding.loginLoginBtn.setOnClickListener {
            presenter.login(
                binding.loginEmailET.text.toString(),
                binding.loginPasswordET.text.toString()
            )
        }

        binding.loginSignupTV.setOnClickListener { goToRegisterActivity() }
    }

    override fun displayEmailError(error: InputErrors?) {
        when (error) {
            InputErrors.FIELD_EMPTY -> {
                binding.loginEmailTIL.error = getString(R.string.empty_field)
            }

            InputErrors.EMAIL_FORMAT_INVALID -> {
                binding.loginEmailTIL.error = getString(R.string.invalid_email)
            }

            InputErrors.INCORRECT_INFO -> {
                binding.loginEmailTIL.error = getString(R.string.incorrect_info)
            }

            else -> binding.loginEmailTIL.error = ""
        }
    }

    override fun displayPasswordError(error: InputErrors?) {
        when (error) {
            InputErrors.FIELD_EMPTY -> {
                binding.loginPasswordTIL.error = getString(R.string.empty_field)
            }

            InputErrors.INCORRECT_INFO -> {
                binding.loginPasswordTIL.error = getString(R.string.incorrect_info)
            }

            else -> binding.loginPasswordTIL.error = ""
        }
    }

    override fun displayLoginButtonProgress(show: Boolean) {
        when (show) {
            true -> {
                binding.loginProgressBar.visibility = View.VISIBLE
                binding.loginLoginBtn.visibility = View.INVISIBLE
            }
            false -> {
                binding.loginProgressBar.visibility = View.INVISIBLE
                binding.loginLoginBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}
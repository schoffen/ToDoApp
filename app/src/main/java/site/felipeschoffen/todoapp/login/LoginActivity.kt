package site.felipeschoffen.todoapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.databinding.ActivityLoginBinding
import site.felipeschoffen.todoapp.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTV.setOnClickListener { goToRegisterActivity() }
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
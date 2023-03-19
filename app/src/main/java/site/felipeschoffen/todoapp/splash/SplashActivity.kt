package site.felipeschoffen.todoapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.databinding.ActivitySplashBinding
import site.felipeschoffen.todoapp.login.LoginActivity
import site.felipeschoffen.todoapp.register.RegisterActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashLoginBtn.setOnClickListener { goToLoginScreen() }
        binding.splashSignupTV.setOnClickListener { goToRegisterScreen() }
    }

    private fun goToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
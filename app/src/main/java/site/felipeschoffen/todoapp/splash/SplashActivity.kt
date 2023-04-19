package site.felipeschoffen.todoapp.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.databinding.ActivitySplashBinding
import site.felipeschoffen.todoapp.login.LoginActivity
import site.felipeschoffen.todoapp.main.MainActivity
import site.felipeschoffen.todoapp.register.RegisterActivity
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            showProgress(true)
            val session = DataSource.getSession()
            if (session) {
                goToMainScreen()
                finish()
            } else {
                showProgress(false)
            }
        }

        binding.splashLoginBtn.setOnClickListener { goToLoginScreen() }
        binding.splashSignupTV.setOnClickListener { goToRegisterScreen() }
    }

    private fun showProgress(show: Boolean) {
        when (show) {
            true -> {
                binding.splashProgress.visibility = View.VISIBLE
                binding.splashLoginBtn.visibility = View.INVISIBLE
                binding.splashSignupTV.visibility = View.INVISIBLE
            }

            false -> {
                binding.splashProgress.visibility = View.GONE
                binding.splashLoginBtn.visibility = View.VISIBLE
                binding.splashSignupTV.visibility = View.VISIBLE
            }
        }
    }

    private fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun goToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}

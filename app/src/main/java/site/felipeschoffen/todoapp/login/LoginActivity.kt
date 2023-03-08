package site.felipeschoffen.todoapp.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package site.felipeschoffen.todoapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.Menu
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This remove default gray items color
        binding.mainBottonNavigation.itemIconTintList = null
    }
}
package site.felipeschoffen.todoapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.CustomDialog
import site.felipeschoffen.todoapp.databinding.ActivityMainBinding
import site.felipeschoffen.todoapp.home.HomeFragment
import site.felipeschoffen.todoapp.profile.ProfileFragment
import site.felipeschoffen.todoapp.splash.SplashActivity
import site.felipeschoffen.todoapp.tasks.TasksFragment

class MainActivity : AppCompatActivity(), Main.View {

    private val presenter = MainPresenter(this)
    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var tasksFragment: TasksFragment
    private lateinit var profileFragment: ProfileFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments()

        // This remove default gray items color
        binding.mainBottonNavigation.itemIconTintList = null

        binding.mainCreateTaskButton.setOnClickListener { openCreateTaskDialog() }

        binding.mainTB.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
                R.id.logout -> logout()
            }
            true
        }

        binding.mainBottonNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    replaceFragment(homeFragment)
                    true
                }

                R.id.menuTasksList -> {
                    replaceFragment(tasksFragment)
                    true
                }
                R.id.menuTaskFolder -> {
                    replaceFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun goToSplashScreen() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    private fun logout() {
        presenter.logout()
    }

    private fun openCreateTaskDialog() {
        CustomDialog.CreateTaskDialog().show(supportFragmentManager, "")
    }

    private fun initFragments() {
        homeFragment = HomeFragment()
        tasksFragment = TasksFragment()
        profileFragment = ProfileFragment()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.mainFragmentContainer.id, fragment)
        fragmentTransaction.commit()
    }
}
package site.felipeschoffen.todoapp.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.common.datas.Task
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.LocalDateTime

class HomeFragment : Fragment(), Home.View {
    private lateinit var binding: FragmentHomeBinding
    private val presenter = HomePresenter(this)
    private val adapter = HomeTasksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeTodayTaskLayout.homeTasksRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayTaskLayout.homeTasksRV.adapter = adapter

        binding.homeTopLayout.homeWelcomeText.text =
            getString(R.string.hello_message, DataSource.userInfo.name)

        presenter.getTodayTasks()
    }

    override fun showProgress(show: Boolean) {
        when (show) {
            true -> binding.homeTodayTaskLayout.homeProgressBar.visibility = View.VISIBLE
            false -> binding.homeTodayTaskLayout.homeProgressBar.visibility = View.GONE
        }
    }


    override fun displayTodayTasks(tasks: List<Task>) {
        showProgress(false)
        binding.homeTodayTaskLayout.homeNoTaskDisplayText.visibility = View.GONE
        binding.homeTodayTaskLayout.homeTasksRV.visibility = View.VISIBLE

        adapter.tasks = tasks
        adapter.notifyDataSetChanged()
    }

    override fun displayEmptyTasks() {
        binding.homeTodayTaskLayout.homeNoTaskDisplayText.visibility = View.VISIBLE
        binding.homeTodayTaskLayout.homeTasksRV.visibility = View.GONE
    }
}
package site.felipeschoffen.todoapp.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import site.felipeschoffen.todoapp.common.CustomDate
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.FragmentTasksBinding
import java.util.*

class TasksFragment : Fragment(), Tasks.View {

    private lateinit var binding: FragmentTasksBinding
    private val presenter = TasksPresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSelectedDateText(CustomDate.dateToString(CustomDate.todayDay, CustomDate.todayMonth, CustomDate.todayYear))

        presenter.getSelectedTasks(SelectedDate(CustomDate.todayDay, CustomDate.todayMonth, CustomDate.todayYear))

        binding.taskDatePickerButton.setOnClickListener {

            val datePickerDialog =
                DatePickerDialog(
                    view.context,
                    { _, year, month, day ->
                        changeSelectedDateText(CustomDate.dateToString(day, month, year))
                        presenter.getSelectedTasks(SelectedDate(day, month, year))
                    },
                    CustomDate.todayYear,
                    CustomDate.todayMonth,
                    CustomDate.todayDay
                )
            datePickerDialog.show()
        }
    }

    override fun showProgress(show: Boolean) {
        when (show) {
            true -> binding.taskProgressBar.visibility = View.VISIBLE
            false -> binding.taskProgressBar.visibility = View.GONE
        }
    }

    override fun displayEmptyTasks() {
        binding.taskDayTaskRV.visibility = View.INVISIBLE
        binding.taskNoTaskDisplayText.visibility = View.VISIBLE
    }

    override fun displayTasks(tasks: List<TasksByHour>) {
        binding.taskNoTaskDisplayText.visibility = View.GONE
        binding.taskDayTaskRV.visibility = View.VISIBLE
        binding.taskDayTaskRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.taskDayTaskRV.adapter = TasksFragmentAdapter(tasks)
    }

    private fun changeSelectedDateText(date: String) {
        binding.taskDatePickerButton.text = date
    }
}
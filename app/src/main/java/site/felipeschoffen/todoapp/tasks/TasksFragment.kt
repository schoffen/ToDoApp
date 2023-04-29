package site.felipeschoffen.todoapp.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import site.felipeschoffen.todoapp.common.CustomTextWatcher
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.common.util.DateTimeUtils.dateToString
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.adapters.TaskAdapterListener
import site.felipeschoffen.todoapp.common.adapters.TasksFragmentAdapter
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.databinding.FragmentTasksBinding

class TasksFragment : Fragment(), Tasks.View, TaskAdapterListener {

    private var currentDate: SelectedDate = SelectedDate(DateTimeUtils.todayDay, DateTimeUtils.todayMonth, DateTimeUtils.todayYear)
    private lateinit var binding: FragmentTasksBinding
    private lateinit var presenter: Tasks.Presenter
    private lateinit var adapter: TasksFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        presenter = TasksPresenter(this, viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSelectedDateText(dateToString(currentDate.day, currentDate.month, currentDate.year))

        presenter.getSelectedTasks(currentDate)

        binding.taskDatePickerButton.setOnClickListener {

            val datePickerDialog =
                DatePickerDialog(
                    view.context,
                    { _, year, month, day ->
                        changeSelectedDateText(dateToString(day, month, year))
                        currentDate = SelectedDate(day, month, year)
                        presenter.getSelectedTasks(currentDate)
                    },
                    DateTimeUtils.todayYear,
                    DateTimeUtils.todayMonth,
                    DateTimeUtils.todayDay
                )
            datePickerDialog.show()
        }

        binding.tasksSearchText.addTextChangedListener(CustomTextWatcher { charSequence ->
            presenter.filterTasksStartWith(charSequence.toString())
        })
    }

    override fun showProgress(show: Boolean) {
        when (show) {
            true -> binding.taskProgressBar.visibility = View.VISIBLE
            false -> binding.taskProgressBar.visibility = View.GONE
        }
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun displayEmptyTasks() {
        binding.taskDayTaskRV.visibility = View.INVISIBLE
        binding.taskNoTaskDisplayText.visibility = View.VISIBLE
    }

    override fun displayTasks(tasks: List<TasksByHour>) {
        binding.taskNoTaskDisplayText.visibility = View.GONE
        binding.taskDayTaskRV.visibility = View.VISIBLE
        binding.taskDayTaskRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = TasksFragmentAdapter(tasks, this, parentFragmentManager, viewLifecycleOwner.lifecycleScope)
        binding.taskDayTaskRV.adapter = adapter
    }

    private fun changeSelectedDateText(date: String) {
        binding.taskDatePickerButton.text = date
    }

    override fun getCurrentDate(): SelectedDate {
        return currentDate
    }

    override fun reloadTasks() {
        presenter.getSelectedTasks(currentDate)
    }

    override fun onDeleteTask(taskUID: String) {
        presenter.deleteTask(taskUID)
    }

    override fun onUpdateTaskStatus(taskUID: String, taskStatus: TaskStatus) {
        presenter.updateTaskStatus(taskUID, taskStatus)
    }

    override fun onEditTask() {
        reloadTasks()
    }
}
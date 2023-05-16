package site.felipeschoffen.todoapp.folderTasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.adapters.FolderTasksAdapter
import site.felipeschoffen.todoapp.common.adapters.TaskAdapterListener
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.dialogs.FilterDialog
import site.felipeschoffen.todoapp.common.dialogs.MonthYearDialog
import site.felipeschoffen.todoapp.common.dialogs.MonthYearDialogListener
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.databinding.ActivityFolderTasksBinding

class FolderTasksActivity() : AppCompatActivity(), FolderTasks.View, TaskAdapterListener {
    lateinit var binding: ActivityFolderTasksBinding
    lateinit var presenter: FolderTasksPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = FolderTasksPresenter(this)

        callSetupViewByFolder(intent.getStringExtra(Constants.EXTRA_FOLDER_ID)!!)

        binding.folderCalendarButton.setOnClickListener { openMonthYearDialog() }
        binding.folderFilterButton.setOnClickListener { openFilterDialog() }
        binding.folderTB.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }

    override fun changeTitleText(title: String) {
        binding.folderNameText.text = title
    }

    override fun changeMonthAndYear(month: Int, year: Int) {
        binding.folderSelectedMonthAndYearText.text =
            DateTimeUtils.formatMonthAndYearIntToString(month, year)
    }

    override fun onDeleteTask(taskUID: String) {
    }

    override fun onUpdateTaskStatus(taskUID: String, taskStatus: TaskStatus) {
    }

    override fun onEditTask() {
    }

    override fun displayUserTasks(userTasksByDateList: List<UserTaskByDate>) {
        val adapter = FolderTasksAdapter(userTasksByDateList.toMutableList(), this, supportFragmentManager, lifecycleScope)
        binding.folderMainRV.adapter = adapter
        binding.folderMainRV.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun displayEmptyTasks() {
    }

    private fun callSetupViewByFolder(folderID: String) {
        presenter.setupViewByFolder(folderID)
    }

    private fun openFilterDialog() {
        FilterDialog().show(supportFragmentManager, null)
    }

    private fun openMonthYearDialog() {
        MonthYearDialog(object : MonthYearDialogListener {
            override fun onDateSelected(month: Int, year: Int) {

            }
        }).show(supportFragmentManager, null)
    }
}
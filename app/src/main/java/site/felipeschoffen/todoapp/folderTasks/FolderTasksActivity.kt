package site.felipeschoffen.todoapp.folderTasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.dialogs.FilterDialog
import site.felipeschoffen.todoapp.common.dialogs.MonthYearDialog
import site.felipeschoffen.todoapp.common.dialogs.MonthYearDialogListener
import site.felipeschoffen.todoapp.databinding.ActivityFolderTasksBinding

class FolderTasksActivity() : AppCompatActivity(), FolderTasks.View {
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
    }

    override fun changeTitleText(title: String) {
        binding.folderNameText.text = title
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
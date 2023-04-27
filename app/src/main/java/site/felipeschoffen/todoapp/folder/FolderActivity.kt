package site.felipeschoffen.todoapp.folder

import android.app.DatePickerDialog
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import site.felipeschoffen.todoapp.common.DateTimeUtils
import site.felipeschoffen.todoapp.common.dialogs.FilterDialog
import site.felipeschoffen.todoapp.databinding.ActivityFolderBinding

class FolderActivity : AppCompatActivity() {
    lateinit var binding: ActivityFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.folderCalendarButton.setOnClickListener { openDatePickerDialog() }
        binding.folderFilterButton.setOnClickListener { openFilterDialog() }
    }

    private fun openDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            {
                    _, year, month, _ ->

            },
            DateTimeUtils.todayYear,
            DateTimeUtils.todayMonth,
            DateTimeUtils.todayDay
        ).show()
    }

    private fun openFilterDialog() {
        FilterDialog().show(supportFragmentManager, null)
    }

}
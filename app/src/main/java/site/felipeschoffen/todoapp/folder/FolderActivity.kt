package site.felipeschoffen.todoapp.folder

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.felipeschoffen.todoapp.common.Date
import site.felipeschoffen.todoapp.common.FilterDialog
import site.felipeschoffen.todoapp.databinding.ActivityFolderBinding

class FolderActivity : AppCompatActivity() {
    lateinit var binding: ActivityFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.folderCalendarButton.setOnClickListener { openCalendar() }
        binding.folderFilterButton.setOnClickListener { openFilterDialog() }
    }

    private fun openCalendar() {
        val datePickerDialog =
            DatePickerDialog(this, null, Date.todayYear, Date.todayMonth, Date.todayDay)
        datePickerDialog.show()
    }

    private fun openFilterDialog() {
        FilterDialog().show(supportFragmentManager, null)
    }

}
package site.felipeschoffen.todoapp.tasks

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Date
import site.felipeschoffen.todoapp.databinding.FragmentTasksBinding
import java.util.Calendar

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeSelectedDateText(Date.dateToString(Date.todayDay, Date.todayMonth, Date.todayYear))

        binding.taskDatePickerButton.setOnClickListener {

            val datePickerDialog =
                DatePickerDialog(
                    view.context,
                    { _, year, month, day ->
                        changeSelectedDateText(Date.dateToString(day, month, year))
                    },
                    Date.todayYear,
                    Date.todayMonth,
                    Date.todayDay
                )
            datePickerDialog.show()
        }
    }

    private fun changeSelectedDateText(date: String) {
        binding.taskDatePickerButton.text = date
    }
}
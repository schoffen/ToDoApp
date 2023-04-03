package site.felipeschoffen.todoapp.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import site.felipeschoffen.todoapp.common.CustomDate
import site.felipeschoffen.todoapp.databinding.FragmentTasksBinding

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

        changeSelectedDateText(CustomDate.dateToString(CustomDate.todayDay, CustomDate.todayMonth, CustomDate.todayYear))

        binding.taskDatePickerButton.setOnClickListener {

            val datePickerDialog =
                DatePickerDialog(
                    view.context,
                    { _, year, month, day ->
                        changeSelectedDateText(CustomDate.dateToString(day, month, year))
                    },
                    CustomDate.todayYear,
                    CustomDate.todayMonth,
                    CustomDate.todayDay
                )
            datePickerDialog.show()
        }
    }

    private fun changeSelectedDateText(date: String) {
        binding.taskDatePickerButton.text = date
    }
}
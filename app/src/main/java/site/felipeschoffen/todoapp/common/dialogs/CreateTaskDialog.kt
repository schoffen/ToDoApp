package site.felipeschoffen.todoapp.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.util.Callback
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.SelectedTime
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.util.PriorityTag
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.DialogCreateTaskBinding
import java.util.*

class CreateTaskDialog(
    private val supportFragmentManager: FragmentManager,
    private val callback: Callback,
    private val coroutineScope: CoroutineScope
) : DialogFragment() {
    private lateinit var binding: DialogCreateTaskBinding
    private val folders = mutableListOf<Folder>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateTaskBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFoldersSpinner()

        var selectedDate =
            SelectedDate(
                DateTimeUtils.todayDay,
                DateTimeUtils.todayMonth,
                DateTimeUtils.todayYear
            )
        var selectedTime = SelectedTime(DateTimeUtils.todayHour, DateTimeUtils.todayMinute)

        changeSelectedDateText(
            DateTimeUtils.dateToString(
                selectedDate.day,
                selectedDate.month,
                selectedDate.year
            )
        )

        changeSelectedTimeText(DateTimeUtils.intToTimestamp(selectedTime.hour, selectedTime.minute))

        // This show custom background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.addDatePickerButton.setOnClickListener {
            datePickerDialog(it) { _, year, month, day ->
                changeSelectedDateText(DateTimeUtils.dateToString(day, month, year))
                selectedDate = SelectedDate(day, month, year)
            }
        }

        binding.addTimePickerButton.setOnClickListener {
            timePickerDialog(it) { _, hour, minute ->

                changeSelectedTimeText(DateTimeUtils.intToTimestamp(hour, minute))

                selectedTime = SelectedTime(hour, minute)
            }
        }

        binding.createCancelButton.setOnClickListener {
            dismiss()
        }

        binding.createYesButton.setOnClickListener {

            val userTask = UserTask(
                uid = UUID.randomUUID().toString(),
                name = binding.createTaskEditText.text.toString(),
                timestamp = getTimestamp(selectedDate, selectedTime),
                priorityTag = getSelectedTag(),
                status = TaskStatus.PENDING,
                folder = folders[binding.createFoldersSpinner.selectedItemPosition]
            )

            coroutineScope.launch {
                val created = DataSource.createTask(userTask)
                if (created)
                    callback.onSuccess()
                else
                    callback.onFailure("Falha ao criar tarefa")

                dismiss()
            }
        }
    }

    private fun datePickerDialog(
        view: View,
        onDateSetListener: DatePickerDialog.OnDateSetListener
    ) {

        val datePickerDialog =
            DatePickerDialog(
                view.context,
                onDateSetListener,
                DateTimeUtils.todayYear,
                DateTimeUtils.todayMonth,
                DateTimeUtils.todayDay
            )
        datePickerDialog.show()
    }

    private fun timePickerDialog(view: View, onTimeSetListener: TimePickerDialog.OnTimeSetListener) {
        val timePickerDialog =
            TimePickerDialog(
                view.context,
                onTimeSetListener,
                DateTimeUtils.todayHour,
                DateTimeUtils.todayMinute,
                true
            )

        timePickerDialog.show()
    }


    private fun changeSelectedDateText(date: String) {
        binding.addDatePickerText.text = date
    }

    private fun changeSelectedTimeText(timestamp: Timestamp) {
        binding.addTimePickerText.text = DateTimeUtils.formatTime(timestamp)
    }

    private fun getTimestamp(
        selectedDate: SelectedDate,
        selectedTime: SelectedTime
    ): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(
            selectedDate.year,
            selectedDate.month,
            selectedDate.day,
            selectedTime.hour,
            selectedTime.minute,
            0
        )

        val date = calendar.time

        return Timestamp(date)
    }

    private fun loadFoldersSpinner() {
        folders.clear()
        folders.addAll(DataSource.getFolders())
        folders.add(0, Constants.NONE_FOLDER_SELECTED_REFERENCE)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, folders.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.createFoldersSpinner.adapter = adapter
    }

    private fun getSelectedTag(): PriorityTag {
        return when (binding.createTagRadioGroup.checkedRadioButtonId) {
            binding.createTagLowPriority.id -> PriorityTag.LOW_PRIORITY
            binding.createTagMediumPriority.id -> PriorityTag.MEDIUM_PRIORITY
            binding.createTagHighPriority.id -> PriorityTag.HIGH_PRIORITY
            binding.createTagUrgent.id -> PriorityTag.URGENT
            else -> PriorityTag.LOW_PRIORITY
        }
    }
}
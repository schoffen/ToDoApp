package site.felipeschoffen.todoapp.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.util.Callback
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.util.PriorityTag
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.DialogEditTaskBinding
import java.util.*

class EditTaskDialog(
    private val supportFragmentManager: FragmentManager,
    private val callback: Callback,
    private val coroutineScope: CoroutineScope,
    private var userTask: UserTask
) : DialogFragment() {
    private lateinit var binding: DialogEditTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFieldsWithUserTask()

        // This show custom background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.editDatePickerButton.setOnClickListener {
            datePickerDialog(it) { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.time = userTask.timestamp.toDate()
                calendar.set(year, month, day)

                userTask.timestamp = Timestamp(calendar.time)
                changeSelectedDateText(userTask.timestamp)
            }
        }

        binding.editTimePickerButton.setOnClickListener {
            timePickerDialog(it) { _, hour, minute ->
                val calendar = Calendar.getInstance()
                calendar.time = userTask.timestamp.toDate()

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                userTask.timestamp = Timestamp(calendar.time)
                changeSelectedTimeText(userTask.timestamp)
            }
        }

        binding.editCancelButton.setOnClickListener {
            dismiss()
        }

        binding.editYesButton.setOnClickListener {
            editTask()
        }
    }

    private fun setupFieldsWithUserTask() {
        binding.editTaskEditText.append(userTask.name)
        changeSelectedDateText(userTask.timestamp)
        changeSelectedTimeText(userTask.timestamp)

        when (userTask.priorityTag) {
            PriorityTag.LOW_PRIORITY -> binding.editTagLowPriority.isChecked = true
            PriorityTag.MEDIUM_PRIORITY -> binding.editTagMediumPriority.isChecked = true
            PriorityTag.HIGH_PRIORITY -> binding.editTagHighPriority.isChecked = true
            PriorityTag.URGENT -> binding.editTagUrgent.isChecked = true
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

    private fun changeSelectedDateText(timestamp: Timestamp) {
        binding.editDatePickerText.text = DateTimeUtils.formatDate(timestamp)
    }

    private fun changeSelectedTimeText(timestamp: Timestamp) {
        binding.editTimePickerText.text = DateTimeUtils.formatTime(timestamp)
    }

    private fun setUserTaskName() {
        userTask.name = binding.editTaskEditText.text.toString()
    }

    private fun setSelectedTag() {
        userTask.priorityTag = when (binding.editTagRadioGroup.checkedRadioButtonId) {
            binding.editTagLowPriority.id -> PriorityTag.LOW_PRIORITY
            binding.editTagMediumPriority.id -> PriorityTag.MEDIUM_PRIORITY
            binding.editTagHighPriority.id -> PriorityTag.HIGH_PRIORITY
            binding.editTagUrgent.id -> PriorityTag.URGENT
            else -> PriorityTag.LOW_PRIORITY
        }
    }

    private fun editTask() {
        setSelectedTag()
        setUserTaskName()

        coroutineScope.launch {
            val edited = DataSource.editTask(userTask)
            if (edited)
                callback.onSuccess()
            else
                callback.onFailure("Falha ao criar tarefa")

            dismiss()
        }
    }

}
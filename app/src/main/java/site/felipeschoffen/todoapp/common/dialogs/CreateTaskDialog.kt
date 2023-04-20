package site.felipeschoffen.todoapp.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.DateTimeUtils
import site.felipeschoffen.todoapp.common.SelectedDate
import site.felipeschoffen.todoapp.common.SelectedTime
import site.felipeschoffen.todoapp.common.adapters.TagsAdapter
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Tag
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

        loadTags()

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

        binding.createTagButton.setOnClickListener {
            CreateTagDialog(object : CreateTagCallback {
                override fun newTagCreated() {
                    loadTags()
                }
            }, coroutineScope).show(supportFragmentManager, null)
        }

        binding.createYesButton.setOnClickListener {

            val userTask = UserTask(
                uid = UUID.randomUUID().toString(),
                name = binding.createTaskEditText.text.toString(),
                timestamp = getTimestamp(selectedDate, selectedTime),
                tags = getSelectedTags(),
                status = TaskStatus.PENDING
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

    private fun loadTags() {
        coroutineScope.launch {
            val userTags = DataSource.getUserTags()

            if (userTags.isNotEmpty()) {
                binding.createTagsRV.adapter = TagsAdapter(userTags)
                binding.createTagsRV.layoutManager =
                    LinearLayoutManager(
                        this@CreateTaskDialog.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                binding.createTagsProgress.visibility = View.GONE
                binding.createTagsRV.visibility = View.VISIBLE
                binding.createNoTagsTV.visibility = View.INVISIBLE
            } else {
                binding.createTagsProgress.visibility = View.GONE
                binding.createNoTagsTV.visibility = View.VISIBLE
            }
        }
    }

    private fun getSelectedTags(): List<Tag> {
        val tags = mutableListOf<Tag>()

        if (binding.createTagsRV.adapter != null) {
            for (i in 0 until binding.createTagsRV.adapter!!.itemCount) {
                val item =
                    binding.createTagsRV.findViewHolderForAdapterPosition(i) as? TagsAdapter.TagViewHolder
                val isChecked =
                    item?.itemView?.findViewById<CheckBox>(R.id.itemTagName)?.isChecked

                if (isChecked == true) {
                    val adapter = binding.createTagsRV.adapter as TagsAdapter

                    val tag = adapter.tags[i]
                    tag.isSelected = true

                    tags.add(tag)
                }
            }
        }

        return tags
    }
}
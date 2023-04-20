package site.felipeschoffen.todoapp.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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

        loadTags()

        binding.editTaskEditText.append(userTask.name)
        changeSelectedDateText(userTask.timestamp)
        changeSelectedTimeText(userTask.timestamp)

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

        binding.editTagButton.setOnClickListener {
            CreateTagDialog(object : CreateTagCallback {
                override fun newTagCreated() {
                    loadTags()
                }
            }, coroutineScope).show(supportFragmentManager, null)
        }

        binding.editYesButton.setOnClickListener {
            setUserTags()
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

    private fun loadTags() {
        coroutineScope.launch {
            val userTags = DataSource.getUserTags()

            userTags.forEach { databaseTag ->
                userTask.tags.forEach { taskTag ->
                    if (databaseTag.uid == taskTag.uid)
                        databaseTag.isSelected = true
                }
            }

            if (userTags.isNotEmpty()) {
                binding.editTagsRV.adapter = TagsAdapter(userTags)
                binding.editTagsRV.layoutManager =
                    LinearLayoutManager(
                        this@EditTaskDialog.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                binding.editTagsProgress.visibility = View.GONE
                binding.editTagsRV.visibility = View.VISIBLE
                binding.editNoTagsTV.visibility = View.INVISIBLE
            } else {
                binding.editTagsProgress.visibility = View.GONE
                binding.editNoTagsTV.visibility = View.VISIBLE
            }
        }
    }

    private fun setUserTaskName() {
        userTask.name = binding.editTaskEditText.text.toString()
    }

    private fun setUserTags() {
        val tags = mutableListOf<Tag>()

        if (binding.editTagsRV.adapter != null) {
            for (i in 0 until binding.editTagsRV.adapter!!.itemCount) {
                val item =
                    binding.editTagsRV.findViewHolderForAdapterPosition(i) as? TagsAdapter.TagViewHolder
                val isChecked =
                    item?.itemView?.findViewById<CheckBox>(R.id.itemTagName)?.isChecked

                if (isChecked == true) {
                    val adapter = binding.editTagsRV.adapter as TagsAdapter

                    val tag = adapter.tags[i]
                    tags.add(tag)
                }
            }

            userTask.tags = tags
        }
    }
}
package site.felipeschoffen.todoapp.common.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.*
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.common.datas.Task
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.databinding.DialogCreateTagBinding
import site.felipeschoffen.todoapp.databinding.DialogCreateTaskBinding
import site.felipeschoffen.todoapp.databinding.DialogFilterBinding
import java.sql.Timestamp
import java.util.*

abstract class CustomDialog {

    class FilterDialog : DialogFragment() {
        private lateinit var binding: DialogFilterBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = DialogFilterBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.filterYesButton.setOnClickListener { this.dismiss() }
        }
    }

    class CreateTaskDialog(private val supportFragmentManager: FragmentManager, private val callback: Callback) : DialogFragment() {
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
                SelectedDate(CustomDate.todayDay, CustomDate.todayMonth, CustomDate.todayYear)
            var selectedTime = SelectedTime(CustomDate.todayHour, CustomDate.todayMinute)

            changeSelectedDateText(
                CustomDate.dateToString(
                    selectedDate.day,
                    selectedDate.month,
                    selectedDate.year
                )
            )

            changeSelectedTimeText(
                CustomDate.timeToString(
                    selectedTime.hour,
                    selectedTime.minute
                )
            )

            // This show custom background
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.addDatePickerButton.setOnClickListener {
                datePickerDialog(it) { _, year, month, day ->
                    changeSelectedDateText(CustomDate.dateToString(day, month, year))
                    selectedDate = SelectedDate(day, month, year)
                }
            }

            binding.addTimePickerButton.setOnClickListener {
                timePickerDialog(it) { _, hour, minute ->
                    // add 0 to hour start
                    val hourString =
                        if (hour.toString().length == 1) "0${hour}" else hour.toString()
                    changeSelectedTimeText("$hourString : $minute")

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
                }).show(supportFragmentManager, null)
            }

            binding.createYesButton.setOnClickListener {

                val task = Task(
                    uid = UUID.randomUUID().toString(),
                    name = binding.createTaskEditText.text.toString(),
                    timestamp = getTimestamp(selectedDate, selectedTime),
                    tags = getSelectedTags(),
                    status = TaskStatus.ON_GOING
                )

                DataSource.createTask(task, object : Callback {
                    override fun onSuccess() {
                        callback.onSuccess()
                    }

                    override fun onFailure(message: String) {
                        callback.onFailure(message)
                    }

                    override fun onComplete() {
                        callback.onComplete()
                        dismiss()
                    }
                })
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
                    CustomDate.todayYear,
                    CustomDate.todayMonth,
                    CustomDate.todayDay
                )
            datePickerDialog.show()
        }

        private fun timePickerDialog(view: View, onTimeSetListener: OnTimeSetListener) {
            val timePickerDialog =
                TimePickerDialog(
                    view.context,
                    onTimeSetListener,
                    CustomDate.todayHour,
                    CustomDate.todayMinute,
                    true
                )

            timePickerDialog.show()
        }


        private fun changeSelectedDateText(date: String) {
            binding.addDatePickerText.text = date
        }

        private fun changeSelectedTimeText(time: String) {
            binding.addTimePickerText.text = time
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

            val date = calendar.time.time

            return Timestamp(date)
        }

        private fun loadTags() {
            DataSource.getUserTags {
                if (it.isNotEmpty()) {
                    binding.createTagsRV.adapter = TagsAdapter(it)
                    binding.createTagsRV.layoutManager =
                        LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                    binding.createTagsProgress.visibility = View.GONE
                    binding.createTagsRV.visibility = View.VISIBLE
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
                        tags.add(tag)
                    }
                }
            }

            return tags
        }
    }

    class CreateTagDialog(private val callback: CreateTagCallback) : DialogFragment() {
        private lateinit var binding: DialogCreateTagBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = DialogCreateTagBinding.inflate(layoutInflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // This show custom background
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.newTagCancelButton.setOnClickListener { dismiss() }
            binding.newTagYesButton.setOnClickListener {

                val uid = UUID.randomUUID().toString()
                val tagName = binding.newTagEditText.text.toString()
                val tagColor: String? = when (binding.newTagRadioGroup.checkedRadioButtonId) {
                    binding.newTagGreenRadio.id -> Constants.tagColorGreen
                    binding.newTagOrangeRadio.id -> Constants.tagColorOrange
                    binding.newTagRedRadio.id -> Constants.tagColorRed
                    binding.newTagPurpleRadio.id -> Constants.tagColorPurple
                    else -> null
                }


                if (tagName.isNotEmpty() && tagColor != null) {
                    DataSource.createTag(Tag(uid, tagName, tagColor), object : Callback {
                        override fun onSuccess() {
                            callback.newTagCreated()
                        }

                        override fun onFailure(message: String) {
                        }

                        override fun onComplete() {
                        }
                    })
                    dismiss()
                } else {
                    Toast.makeText(
                        view.context,
                        "Tag deve conter um nome e uma cor",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import site.felipeschoffen.todoapp.common.Date
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.database.Tag
import site.felipeschoffen.todoapp.databinding.DialogCreateTagBinding
import site.felipeschoffen.todoapp.databinding.DialogCreateTaskBinding
import site.felipeschoffen.todoapp.databinding.DialogFilterBinding

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

    class CreateTaskDialog(private val supportFragmentManager: FragmentManager) : DialogFragment() {
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

            DataSource.getUserTags {
                binding.createTagsRV.adapter = TagsAdapter(it)
                binding.createTagsRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                binding.createTagsRV.visibility = View.VISIBLE
                binding.createNoTagsTV.visibility = View.GONE
            }

            // This show custom background
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.addDatePickerButton.setOnClickListener {
                datePickerDialog(it)
            }

            binding.addTimePickerButton.setOnClickListener {
                timePickerDialog(it)
            }

            binding.createTagButton.setOnClickListener {
                CreateTagDialog().show(supportFragmentManager, null)
            }
        }

        private fun datePickerDialog(view: View) {
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

        private fun timePickerDialog(view: View) {
            val timePickerDialog =
                TimePickerDialog(
                    view.context,
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        // add 0 to hour start
                        val hourString = if (hour.toString().length == 1) "0${hour.toString()}" else hour.toString()
                        changeSelectedTimeText("$hourString : ${minute.toString()}")
                    },
                    Date.todayHour,
                    Date.todayMinute,
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
    }

    class CreateTagDialog : DialogFragment() {
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
                val tagName = binding.newTagEditText.text.toString()

                if (tagName.isNotEmpty()) {
                    DataSource.createTag(Tag(tagName))
                    dismiss()
                } else {
                    Toast.makeText(view.context, "Tag deve conter um nome", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
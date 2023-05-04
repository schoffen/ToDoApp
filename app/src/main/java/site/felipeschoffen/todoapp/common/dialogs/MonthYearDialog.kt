package site.felipeschoffen.todoapp.common.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.databinding.DialogCreateFolderBinding
import site.felipeschoffen.todoapp.databinding.DialogMonthYearPickerBinding
import java.util.*

class MonthYearDialog(private val listener: MonthYearDialogListener?): DialogFragment() {
    private lateinit var binding: DialogMonthYearPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMonthYearPickerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()

        binding.pickerCancelButton.setOnClickListener { dismiss() }
        binding.pickerYesButton.setOnClickListener {
            callListener()
            dismiss()
        }
    }

    private fun setupLayout() {
        setupYearPicker()
        setupMonthPicker()

        // This show custom background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupYearPicker() {
        binding.yearPicker.minValue = 2023
        binding.yearPicker.maxValue = DateTimeUtils.todayYear + 10
        binding.yearPicker.value = DateTimeUtils.todayYear
        binding.yearPicker.wrapSelectorWheel = false
    }

    private fun setupMonthPicker() {
        binding.monthPicker.minValue = 0
        binding.monthPicker.maxValue = 11
        binding.monthPicker.displayedValues = binding.root.context.resources.getStringArray(R.array.months)
        binding.monthPicker.wrapSelectorWheel = false
    }

    private fun callListener() {
        val month = binding.monthPicker.value
        val year = binding.yearPicker.value
        listener?.onDateSelected(month, year)
    }

}
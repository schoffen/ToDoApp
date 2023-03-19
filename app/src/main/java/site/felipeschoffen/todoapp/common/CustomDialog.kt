package site.felipeschoffen.todoapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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

    class CreateTaskDialog : DialogFragment() {
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
        }
    }
}
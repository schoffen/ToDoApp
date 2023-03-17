package site.felipeschoffen.todoapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import site.felipeschoffen.todoapp.databinding.DialogCreateTaskBinding

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
}
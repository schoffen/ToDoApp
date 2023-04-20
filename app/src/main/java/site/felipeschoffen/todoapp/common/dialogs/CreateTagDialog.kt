package site.felipeschoffen.todoapp.common.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.databinding.DialogCreateTagBinding
import java.util.*

class CreateTagDialog(
    private val callback: CreateTagCallback,
    private val coroutineScope: CoroutineScope
) : DialogFragment() {
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
                coroutineScope.launch {
                    val tagCreated = DataSource.createTag(Tag(uid, tagName, tagColor))
                    if (tagCreated)
                        callback.newTagCreated()
                }

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
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
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.databinding.DialogCreateFolderBinding
import java.util.*

class CreateFolderDialog(
    private val callback: CreateFolderCallback,
    private val coroutineScope: CoroutineScope
) : DialogFragment() {
    private lateinit var binding: DialogCreateFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateFolderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This show custom background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.newFolderCancelButton.setOnClickListener { dismiss() }
        binding.newFolderYesButton.setOnClickListener {

            val uid = UUID.randomUUID().toString()
            val folderName = binding.newFolderEditText.text.toString()
            val folderColor: String = when (binding.newFolderRadioGroup.checkedRadioButtonId) {
                binding.newFolderGreenRadio.id -> Constants.COLOR_GREEN
                binding.newFolderOrangeRadio.id -> Constants.COLOR_ORANGE
                binding.newFolderRedRadio.id -> Constants.COLOR_RED
                binding.newFolderPurpleRadio.id -> Constants.COLOR_PURPLE
                else -> Constants.COLOR_ORANGE
            }

            if (folderName.isNotEmpty()) {
                coroutineScope.launch {
                    val folderToAdd = Folder(uid, folderName, folderColor)
                    val folderAdded = DataSource.addFolder(folderToAdd)
                    if (folderAdded)
                        callback.newFolderCreated(folderToAdd)
                }

                dismiss()
            } else {
                Toast.makeText(
                    view.context,
                    "Pasta deve conter um nome e uma cor",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
package site.felipeschoffen.todoapp.common.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.databinding.DialogDeleteFolderBinding

class DeleteFolderDialog(
    private val callback: DeleteFolderCallback,
    private val coroutineScope: CoroutineScope,
    private val folder: Folder
) : DialogFragment() {
    private lateinit var binding: DialogDeleteFolderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteFolderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This show custom background
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.deleteFolderNameTV.text = getString(R.string.delete_folder, folder.name)

        binding.deleteFolderCancelButton.setOnClickListener { dismiss() }
        binding.deleteFolderYesButton.setOnClickListener {
            coroutineScope.launch {
                DataSource.deleteFolderAndTasksInFolder(folder)
            }
            callback.folderDeleted()
            dismiss()
        }
    }
}
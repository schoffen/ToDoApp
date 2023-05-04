package site.felipeschoffen.todoapp.common.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Folder
import site.felipeschoffen.todoapp.common.dialogs.CreateFolderCallback
import site.felipeschoffen.todoapp.common.dialogs.CreateFolderDialog
import site.felipeschoffen.todoapp.common.dialogs.DeleteFolderCallback
import site.felipeschoffen.todoapp.common.dialogs.DeleteFolderDialog
import site.felipeschoffen.todoapp.databinding.ItemFolderBinding
import site.felipeschoffen.todoapp.profile.Profile

class FoldersAdapter(
    val folders: MutableList<Folder>,
    private val coroutineScope: CoroutineScope,
    private val view: Profile.View,
    private val supportFragmentManager: FragmentManager
) :
    RecyclerView.Adapter<FoldersAdapter.FoldersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersViewHolder {
        val binding =
            ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FoldersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoldersViewHolder, position: Int) {
        val item = folders[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    inner class FoldersViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(folder: Folder) {
            if (folder.uid == Constants.CREATE_NEW_FOLDER_REFERENCE.uid) {
                binding.folderIconIV.setImageResource(R.drawable.ic_plus)
                binding.folderNameText.text =
                    this.itemView.context.getString(R.string.create_folder)

                binding.folderCL.setOnClickListener {
                    if (folder.uid == Constants.CREATE_NEW_FOLDER_REFERENCE.uid) {
                        openCreateFolderDialog()
                    }
                }
            } else {
                binding.folderIconIV.setImageResource(R.drawable.ic_folder)
                binding.folderNameText.text = folder.name

                val colors = selectColorByString(folder.color)

                ViewCompat.setBackgroundTintList(
                    binding.folderIconIV,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this.binding.root.context,
                            colors.first
                        )
                    )
                )

                ViewCompat.setBackgroundTintList(
                    binding.folderCL,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this.binding.root.context,
                            colors.second
                        )
                    )
                )

                binding.folderCL.setOnClickListener {
                    // Ir para a pasta
                }

                binding.folderCL.setOnLongClickListener {
                    openDeleteFolderDialog(folder, adapterPosition)
                    true
                }
            }
        }

        private fun openCreateFolderDialog() {
            CreateFolderDialog(object : CreateFolderCallback {
                override fun newFolderCreated(folder: Folder) {
                    val index = folders.size - 1
                    folders.add(index, folder)
                    view.notifyAdapterItemInserted(index)
                }
            }, coroutineScope).show(supportFragmentManager, null)
        }

        private fun openDeleteFolderDialog(folder: Folder, position: Int) {
            DeleteFolderDialog(object : DeleteFolderCallback {
                override fun folderDeleted() {
                    folders.removeAt(position)
                    view.notifyAdapterItemRemoved(position)
                }
            }, coroutineScope, folder).show(supportFragmentManager, null)
        }

        @ColorRes
        private fun selectColorByString(folderColor: String): Pair<Int, Int> {
            return when (folderColor) {
                Constants.COLOR_ORANGE -> Pair(R.color.orange_dark, R.color.orange_light)
                Constants.COLOR_RED -> Pair(R.color.red_dark, R.color.red_light)
                Constants.COLOR_PURPLE -> Pair(R.color.purple_dark, R.color.purple_light)
                Constants.COLOR_GREEN -> Pair(R.color.green_dark, R.color.green_light)
                else -> Pair(R.color.orange_dark, R.color.orange_light)
            }
        }
    }
}
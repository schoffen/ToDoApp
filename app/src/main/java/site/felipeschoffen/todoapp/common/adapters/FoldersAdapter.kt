package site.felipeschoffen.todoapp.common.adapters

import android.content.res.ColorStateList
import android.util.Log
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
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.dialogs.CreateFolderCallback
import site.felipeschoffen.todoapp.common.dialogs.CreateFolderDialog
import site.felipeschoffen.todoapp.databinding.ItemFolderBinding
import site.felipeschoffen.todoapp.profile.Profile

class FoldersAdapter(
    var folders: List<Folder>,
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

        val item: Folder = if (position == folders.size) {
            Folder("add", "add")
        } else {
            folders[position]
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return folders.size + 1
    }

    inner class FoldersViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(folder: Folder) {
            if (folder.uid == "add") {
                binding.folderIconIV.setImageResource(R.drawable.ic_plus)
                binding.folderNameText.text =
                    this.itemView.context.getString(R.string.create_folder)

                binding.folderCL.setOnClickListener {
                    openCreateFolderDialog()
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
            }
        }

        private fun openCreateFolderDialog() {
            CreateFolderDialog(object : CreateFolderCallback {
                override fun newFolderCreated() {
                    view.getFolders()
                }
            }, coroutineScope).show(supportFragmentManager, null)
        }

        @ColorRes
        private fun selectColorByString(folderColor: String): Pair<Int, Int> {
            return when (folderColor) {
                Constants.colorOrange -> Pair(R.color.orange_dark, R.color.orange_light)
                Constants.colorRed -> Pair(R.color.red_dark, R.color.red_light)
                Constants.colorPurple -> Pair(R.color.purple_dark, R.color.purple_light)
                Constants.colorGreen -> Pair(R.color.green_dark, R.color.green_light)
                else -> Pair(R.color.orange_dark, R.color.orange_light)
            }
        }
    }
}
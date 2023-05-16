package site.felipeschoffen.todoapp.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import site.felipeschoffen.todoapp.common.util.DateTimeUtils
import site.felipeschoffen.todoapp.databinding.ItemFolderTaskBinding
import site.felipeschoffen.todoapp.folderTasks.UserTaskByDate

class FolderTasksAdapter(
    private val userTaskByDateList: MutableList<UserTaskByDate>,
    private val listener: TaskAdapterListener,
    private val supportFragmentManager: FragmentManager,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<FolderTasksAdapter.FolderTasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderTasksViewHolder {
        val binding =
            ItemFolderTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FolderTasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderTasksViewHolder, position: Int) {
        val item = userTaskByDateList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return userTaskByDateList.size
    }

    inner class FolderTasksViewHolder(private val binding: ItemFolderTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userTaskByDate: UserTaskByDate) {
            binding.folderDateText.text = DateTimeUtils.formatDate(userTaskByDate.timestamp)

            val tasksAdapter = ShortTaskAdapter(listener, supportFragmentManager, coroutineScope)
            tasksAdapter.userTasks = userTaskByDate.userTasksList

            binding.folderTasksRV.adapter = tasksAdapter
            binding.folderTasksRV.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
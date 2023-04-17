package site.felipeschoffen.todoapp.common.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.DateTimeUtils.formatTime
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.ItemTaskShortBinding

class ShortTaskAdapter(private val listener: TaskAdapterListener) : RecyclerView.Adapter<ShortTaskAdapter.TaskViewHolder>() {
    lateinit var userTasks: List<UserTask>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            ItemTaskShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = userTasks[position]
        holder.bind(item)
    }

    override fun getItemCount() = userTasks.size

    inner class TaskViewHolder(private val binding: ItemTaskShortBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userTask: UserTask) {
            binding.itemTodayTaskName.text = userTask.name
            binding.itemTodayTaskTime.text = formatTime(userTask.timestamp)

            ViewCompat.setBackgroundTintList(
                binding.itemTodayTaskLine,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this.binding.root.context,
                        selectColorByStatus(userTask.status)
                    )
                )
            )

            binding.itemTodayTaskTagsRV.layoutManager = LinearLayoutManager(
                this.binding.root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.itemTodayTaskTagsRV.adapter = TagsWideAdapter(userTask.tags)

            binding.itemTodayTaskItemSpinner.setOnClickListener {
                val popUp = PopupMenu(binding.root.context, binding.itemTodayTaskItemSpinner)
                popUp.inflate(R.menu.menu_task)

                when (userTask.status) {
                    TaskStatus.COMPLETED -> popUp.menu.findItem(R.id.menuTaskRestore).isVisible = true
                    TaskStatus.CANCELED -> {
                        popUp.menu.findItem(R.id.menuTaskRestore).isVisible = true
                        popUp.menu.findItem(R.id.menuTaskCancel).isVisible = false
                    }
                    TaskStatus.PENDING -> {
                        popUp.menu.findItem(R.id.menuTaskStart).isVisible = true
                        popUp.menu.findItem(R.id.menuTaskEdit).isVisible = true
                    }
                    TaskStatus.ON_GOING -> popUp.menu.findItem(R.id.menuTaskComplete).isVisible = true
                }

                popUp.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menuTaskDelete -> {
                            listener.onDeleteTask(userTask.uid)
                            true
                        }

                        R.id.menuTaskCancel -> {
                            listener.onUpdateTaskStatus(userTask.uid, TaskStatus.CANCELED)
                            true
                        }

                        else -> false
                    }
                }

                popUp.show()
            }
        }

        @ColorRes
        private fun selectColorByStatus(status: TaskStatus): Int {
            return when (status) {
                TaskStatus.COMPLETED -> R.color.blue_full
                TaskStatus.CANCELED -> R.color.red_full
                TaskStatus.PENDING -> R.color.purple_full
                TaskStatus.ON_GOING -> R.color.green_full
            }
        }
    }
}
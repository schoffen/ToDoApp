package site.felipeschoffen.todoapp.common.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.DateTimeUtils.formatTime
import site.felipeschoffen.todoapp.common.database.DataSource
import site.felipeschoffen.todoapp.common.datas.TaskStatus
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.common.dialogs.EditTaskDialog
import site.felipeschoffen.todoapp.databinding.ItemTaskShortBinding

class ShortTaskAdapter(
    private val listener: TaskAdapterListener,
    private val supportFragmentManager: FragmentManager,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<ShortTaskAdapter.TaskViewHolder>() {
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

            val colors = selectColorByStatus(userTask.status)

            ViewCompat.setBackgroundTintList(
                binding.itemTodayTaskLine,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this.binding.root.context,
                        colors.first
                    )
                )
            )

            ViewCompat.setBackgroundTintList(
                binding.itemCL,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this.binding.root.context,
                        colors.second
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
                    TaskStatus.COMPLETED -> popUp.menu.findItem(R.id.menuTaskRestore).isVisible =
                        true
                    TaskStatus.CANCELED -> {
                        popUp.menu.findItem(R.id.menuTaskRestore).isVisible = true
                        popUp.menu.findItem(R.id.menuTaskCancel).isVisible = false
                    }
                    TaskStatus.PENDING -> {
                        popUp.menu.findItem(R.id.menuTaskStart).isVisible = true
                        popUp.menu.findItem(R.id.menuTaskEdit).isVisible = true
                    }
                    TaskStatus.ON_GOING -> popUp.menu.findItem(R.id.menuTaskComplete).isVisible =
                        true
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

                        R.id.menuTaskStart -> {
                            listener.onUpdateTaskStatus(userTask.uid, TaskStatus.ON_GOING)
                            true
                        }

                        R.id.menuTaskComplete -> {
                            listener.onUpdateTaskStatus(userTask.uid, TaskStatus.COMPLETED)
                            true
                        }

                        R.id.menuTaskRestore -> {
                            listener.onUpdateTaskStatus(userTask.uid, TaskStatus.PENDING)
                            true
                        }

                        R.id.menuTaskEdit -> {
                            openEditTaskDialog(userTask)
                            true
                        }

                        else -> false
                    }
                }

                popUp.show()
            }
        }

        /**
         *
         * Return Pair<Strong color, light color>
         *     common used for <Vertical Line, Background>
         */
        @ColorRes
        private fun selectColorByStatus(status: TaskStatus): Pair<Int, Int> {
            return when (status) {
                TaskStatus.COMPLETED -> Pair(R.color.blue_full, R.color.blue_alpha)
                TaskStatus.CANCELED -> Pair(R.color.red_full, R.color.red_alpha)
                TaskStatus.PENDING -> Pair(R.color.purple_full, R.color.purple_alpha)
                TaskStatus.ON_GOING -> Pair(R.color.green_full, R.color.green_alpha)
            }
        }

        private fun openEditTaskDialog(userTask: UserTask) {
            EditTaskDialog(supportFragmentManager, object : Callback {
                override fun onSuccess() {
                    listener.onEditTask()
                }

                override fun onFailure(message: String) {
                }

                override fun onComplete() {
                }
            }, coroutineScope, userTask).show(supportFragmentManager, null)
        }
    }
}
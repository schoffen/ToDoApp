package site.felipeschoffen.todoapp.common.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Callback
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.util.DateTimeUtils.formatTime
import site.felipeschoffen.todoapp.common.datas.Folder
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

            val statusColors = selectColorByStatus(userTask.status)

            ViewCompat.setBackgroundTintList(
                binding.itemTodayTaskLine,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this.binding.root.context,
                        statusColors.first
                    )
                )
            )

            ViewCompat.setBackgroundTintList(
                binding.itemCL,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this.binding.root.context,
                        statusColors.second
                    )
                )
            )

            binding.itemTodayTaskTag.itemTagName.text = userTask.priorityTag.getTagNameInPortuguese(binding.root.context)
            binding.itemTodayTaskTag.itemTagName.setTextColor(binding.root.context.getColor(userTask.priorityTag.getPriorityColor().textColor))

            ViewCompat.setBackgroundTintList(
                binding.itemTodayTaskTag.itemTagRoot,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        userTask.priorityTag.getPriorityColor().backgroundColor
                    )
                )
            )

            if (userTask.folder != null && userTask.folder!!.uid != "null") {
                
                binding.itemFolderName.visibility = View.VISIBLE

                binding.itemFolderName.text = userTask.folder!!.name

                val folderColors = selectColorByFolder(userTask.folder!!)

                binding.itemFolderName.setTextColor(folderColors.first)

                ViewCompat.setBackgroundTintList(
                    binding.itemFolderName,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this.binding.root.context,
                            folderColors.second
                        )
                    )
                )
            } else {
                binding.itemFolderName.visibility = View.GONE
            }

            binding.itemCL.setOnLongClickListener {
                val popUp = PopupMenu(binding.root.context, binding.itemCL)
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

                true
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

        @ColorRes
        private fun selectColorByFolder(folder: Folder): Pair<Int, Int> {
            return when (folder.color) {
                Constants.colorOrange -> Pair(R.color.orange_dark, R.color.orange_light)
                Constants.colorGreen -> Pair(R.color.green_dark, R.color.green_light)
                Constants.colorRed -> Pair(R.color.red_dark, R.color.red_light)
                Constants.colorPurple -> Pair(R.color.purple_dark, R.color.purple_light)
                else -> Pair(R.color.orange_dark, R.color.orange_light)
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
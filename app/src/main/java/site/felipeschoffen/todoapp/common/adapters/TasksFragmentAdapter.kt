package site.felipeschoffen.todoapp.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.felipeschoffen.todoapp.common.DateTimeUtils.formatTime
import site.felipeschoffen.todoapp.common.DateTimeUtils.intToTimestamp
import site.felipeschoffen.todoapp.databinding.ItemTaskDayHourBinding
import site.felipeschoffen.todoapp.tasks.TasksByHour

class TasksFragmentAdapter(var tasksByHour: List<TasksByHour>, val listener: TaskAdapterListener) :
    RecyclerView.Adapter<TasksFragmentAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding =
            ItemTaskDayHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = tasksByHour[position]
        holder.bind(item)
    }

    override fun getItemCount() = tasksByHour.size

    inner class TaskViewHolder(private val binding: ItemTaskDayHourBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tasksByHour: TasksByHour) {
            binding.timeText.text = formatTime(intToTimestamp(tasksByHour.hour, 0))

            binding.hoursTasksRV.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            val adapter = ShortTaskAdapter(listener)
            adapter.userTasks = tasksByHour.userTasksList.sortedBy { it.timestamp.toDate().minutes }
            binding.hoursTasksRV.adapter = adapter
        }


    }
}
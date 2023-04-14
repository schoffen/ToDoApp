package site.felipeschoffen.todoapp.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.CustomDate.formatTime
import site.felipeschoffen.todoapp.common.ShortTaskAdapter
import site.felipeschoffen.todoapp.databinding.ItemTaskDayHourBinding
import java.util.*

class TasksFragmentAdapter(var tasksByHour: List<TasksByHour>) :
    RecyclerView.Adapter<TasksFragmentAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksFragmentAdapter.TaskViewHolder {
        val binding =
            ItemTaskDayHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksFragmentAdapter.TaskViewHolder, position: Int) {
        val item = tasksByHour[position]
        holder.bind(item)
    }

    override fun getItemCount() = tasksByHour.size

    inner class TaskViewHolder(private val binding: ItemTaskDayHourBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tasksByHour: TasksByHour) {
            binding.timeText.text = formatTime(Timestamp(tasksByHour.hour.toLong() * 3600, 0))

            binding.hoursTasksRV.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)

            val adapter = ShortTaskAdapter()
            adapter.userTasks = tasksByHour.userTasksList.sortedBy { it.timestamp.toDate().minutes }
            binding.hoursTasksRV.adapter = adapter
        }


    }
}
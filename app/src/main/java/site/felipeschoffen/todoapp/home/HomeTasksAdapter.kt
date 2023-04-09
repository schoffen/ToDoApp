package site.felipeschoffen.todoapp.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.datas.Task
import site.felipeschoffen.todoapp.common.dialogs.TagsAdapter
import site.felipeschoffen.todoapp.databinding.ItemTagBinding
import site.felipeschoffen.todoapp.databinding.ItemTaskWideBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeTasksAdapter() : RecyclerView.Adapter<HomeTasksAdapter.TaskViewHolder>() {
    lateinit var tasks: List<Task>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTasksAdapter.TaskViewHolder {
        val binding = ItemTaskWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("holder", "holder")
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeTasksAdapter.TaskViewHolder, position: Int) {
        val item = tasks[position]
        holder.bind(item)
    }

    override fun getItemCount() = tasks.size

    inner class TaskViewHolder(private val binding: ItemTaskWideBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task) {
            Log.d("adapter", task.toString())
            binding.itemTodayTaskName.text = task.name
            binding.itemTodayTaskTime.text = timestampToString(task.timestamp)

            binding.itemTodayTaskTagsRV.layoutManager = LinearLayoutManager(this.binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.itemTodayTaskTagsRV.adapter = TagsAdapter(task.tags)
        }

        private fun timestampToString(timestamp: Timestamp): String {
            val date = timestamp.toDate()

            val calendar = Calendar.getInstance()
            calendar.time = date

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return "$hour : $minute"
        }
    }
}
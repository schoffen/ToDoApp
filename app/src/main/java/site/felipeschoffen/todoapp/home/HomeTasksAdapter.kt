package site.felipeschoffen.todoapp.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.ItemTaskWideBinding
import java.util.*

class HomeTasksAdapter() : RecyclerView.Adapter<HomeTasksAdapter.TaskViewHolder>() {
    lateinit var userTasks: List<UserTask>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTasksAdapter.TaskViewHolder {
        val binding = ItemTaskWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("holder", "holder")
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeTasksAdapter.TaskViewHolder, position: Int) {
        val item = userTasks[position]
        holder.bind(item)
    }

    override fun getItemCount() = userTasks.size

    inner class TaskViewHolder(private val binding: ItemTaskWideBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(userTask: UserTask) {
            Log.d("adapter", userTask.toString())
            binding.itemTodayTaskName.text = userTask.name
            binding.itemTodayTaskTime.text = timestampToString(userTask.timestamp)

            binding.itemTodayTaskTagsRV.layoutManager = LinearLayoutManager(this.binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.itemTodayTaskTagsRV.adapter = TagsWideAdapter(userTask.tags)
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
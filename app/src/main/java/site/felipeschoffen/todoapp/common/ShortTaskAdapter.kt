package site.felipeschoffen.todoapp.common

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import site.felipeschoffen.todoapp.common.datas.UserTask
import site.felipeschoffen.todoapp.databinding.ItemTaskShortBinding
import site.felipeschoffen.todoapp.databinding.ItemTaskWideBinding
import site.felipeschoffen.todoapp.home.TagsWideAdapter
import java.util.*

class ShortTaskAdapter() : RecyclerView.Adapter<ShortTaskAdapter.TaskViewHolder>() {
    lateinit var userTasks: List<UserTask>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskShortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = userTasks[position]
        holder.bind(item)
    }

    override fun getItemCount() = userTasks.size

    inner class TaskViewHolder(private val binding: ItemTaskShortBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(userTask: UserTask) {
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
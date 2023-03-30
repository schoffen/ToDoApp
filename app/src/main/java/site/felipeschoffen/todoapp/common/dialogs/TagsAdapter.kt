package site.felipeschoffen.todoapp.common.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import site.felipeschoffen.todoapp.common.database.Tag
import site.felipeschoffen.todoapp.databinding.ItemTagBinding

class TagsAdapter(private val tags: List<Tag>) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.ViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsAdapter.ViewHolder, position: Int) {
        val item = tags[position]
        holder.bind(item)
    }

    override fun getItemCount() = tags.size

    inner class ViewHolder(private val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tag: Tag) {
            binding.itemTagName.text = tag.name
        }
    }
}
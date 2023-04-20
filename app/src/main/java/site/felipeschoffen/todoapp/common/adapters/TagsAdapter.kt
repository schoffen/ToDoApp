package site.felipeschoffen.todoapp.common.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.databinding.ItemTagBinding

class TagsAdapter(val tags: List<Tag>) : RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val item = tags[position]
        holder.bind(item)
    }

    override fun getItemCount() = tags.size

    inner class TagViewHolder(private val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tag: Tag) {
            binding.itemTagName.text = tag.name
            binding.itemTagName.isChecked = tag.isSelected

            when(tag.color) {
                Constants.tagColorGreen -> {
                    binding.itemTagName.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_green)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_green))
                }

                Constants.tagColorOrange -> {
                    binding.itemTagName.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_orange)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_orange))
                }

                Constants.tagColorRed -> {
                    binding.itemTagName.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_red)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_red))
                }

                Constants.tagColorPurple -> {
                    binding.itemTagName.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_purple)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_purple))
                }
            }
        }
    }
}
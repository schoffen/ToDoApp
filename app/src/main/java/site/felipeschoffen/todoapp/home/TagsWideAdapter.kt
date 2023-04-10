package site.felipeschoffen.todoapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import site.felipeschoffen.todoapp.R
import site.felipeschoffen.todoapp.common.Constants
import site.felipeschoffen.todoapp.common.datas.Tag
import site.felipeschoffen.todoapp.databinding.ItemTagBinding
import site.felipeschoffen.todoapp.databinding.ItemTagWideBinding

class TagsWideAdapter(val tags: List<Tag>) : RecyclerView.Adapter<TagsWideAdapter.TagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsWideAdapter.TagViewHolder {
        val binding = ItemTagWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsWideAdapter.TagViewHolder, position: Int) {
        val item = tags[position]
        holder.bind(item)
    }

    override fun getItemCount() = tags.size

    inner class TagViewHolder(private val binding: ItemTagWideBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tag: Tag) {
            binding.itemTagName.text = tag.name

            when(tag.color) {
                Constants.tagColorGreen -> {
                    binding.itemTagRoot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_green)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_green))
                }

                Constants.tagColorOrange -> {
                    binding.itemTagRoot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_orange)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_orange))
                }

                Constants.tagColorRed -> {
                    binding.itemTagRoot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_red)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_red))
                }

                Constants.tagColorPurple -> {
                    binding.itemTagRoot.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selector_background_tag_purple)
                    binding.itemTagName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.tag_text_purple))
                }
            }
        }
    }
}
package com.example.mystoryapp.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.local.room.entity.Story
import com.example.mystoryapp.databinding.ListStoryItemBinding
import com.example.mystoryapp.view.detailstory.DetailStoryActivity

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.MyViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    class MyViewHolder(private val binding: ListStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: Story) {
            with (binding) {
                tvItemName.text = storyItem.name
                tvItemDescription.text = storyItem.description
                Glide.with(ivItemPhoto.context)
                    .load(storyItem.photoUrl)
                    .into(ivItemPhoto)

                // when item clicked, go to detail story
                itemView.setOnClickListener{ view ->
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "imageViewDetailStory"),
                            Pair(tvItemName, "textViewTitleDetailStory"),
                            Pair(tvItemDescription, "textViewDescDetailStory")
                        )
                    val intent = Intent(view.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_ID, storyItem.id)
                    view.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }


        }
    }

}
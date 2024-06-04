package com.example.wildan_canary.helper.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.databinding.RecycleStoryBinding
import com.example.wildan_canary.helper.adapter.AdapterStory.MyViewHolder
import com.example.wildan_canary.view.story.detail.DetailStoryActivity

class AdapterStory(private val callback: (story: ItemStory, imageView: View, nameView: View, descView: View) -> Unit)
    : PagingDataAdapter<ItemStory, MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RecycleStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(holder.itemView.context,user)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemStory>() {
            override fun areItemsTheSame(oldItem: ItemStory, newItem: ItemStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemStory, newItem: ItemStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
        const val DETAIL_STORY = "storycanary"

        fun parseAddressLocation(
            context: Context,
            lat: Double,
            lon: Double
        ): String {
            var address = ""
            try {
                val geocoder = Geocoder(context)
                val geoLocation =
                    geocoder.getFromLocation(lat, lon, 1)
                if (geoLocation != null) {
                    address = if (geoLocation.size > 0) {
                        val location = geoLocation[0]
                        val fullAddress = location.getAddressLine(0)
                        StringBuilder("📌 ")
                            .append(fullAddress).toString()
                    } else {
                        "📌 Location Unknown"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return address
        }
    }
    class MyViewHolder( val binding: RecycleStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private fun String.timeStamptoString(): String = substring(0, 10)
        fun bind(context: Context, story: ItemStory){
            with(binding){
                tvItemName.text = story.name
                tvDescStory.text = story.description
                tvDateStory.text = story.createdAt.timeStamptoString()
                Glide.with(binding.root.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(ivItemPhoto)
                if (story.lat != null && story.lon != null) {
                    tvLocation.visibility = View.VISIBLE
                    tvLocation.text = parseAddressLocation(context , story.lat, story.lon)
                } else {
                    tvLocation.visibility = View.GONE
                }
            }
            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.ivItemPhoto as View, "thumbnail"),
                        Pair(binding.tvItemName as View, "title"),
                        Pair(binding.tvDescStory as View, "description"),
                        Pair(binding.tvLocation as View, "location")
                    )
                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                intent.putExtra(DETAIL_STORY,story.id)
                binding.root.context.startActivity(intent,optionsCompat.toBundle())

            }
        }
    }
}


package com.example.wildan_canary.view.story.detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivityDetailStoryBinding
import com.example.wildan_canary.helper.ViewModelFactory
import com.example.wildan_canary.view.story.view.ViewStoryViewModel
import com.example.wildan_canary.data.network.response.Result
import com.example.wildan_canary.helper.adapter.AdapterStory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val detailViewModel: DetailStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(DETAIL_STORY)


        if(id != null){
            detailViewModel.getDetailStory(id)
        }
        detailViewModel.story.observe(this){
                story ->
            when(story){
                is Result.Loading -> {
                    binding.detailProgresBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.apply {
                        detailProgresBar.visibility = View.GONE
                        tvDetailName.text = story.data.name
                        tvDetailDescription.text = story.data.description
                        if (story.data.lat != null && story.data.lon != null){
                            tvViewLocation.visibility = View.VISIBLE
                            tvViewLocation.text = story.data.lon.let {
                                AdapterStory.parseAddressLocation(this@DetailStoryActivity,
                                    story.data.lat, it
                                )
                            }
                        }
                        else {
                            tvViewLocation.visibility = View.GONE
                        }
                        ivDetailPhoto.setImageUrl(story.data.photoUrl, true)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, story.error, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    fun ImageView.setImageUrl(url: String, isCenterCrop: Boolean) {
        val glideRequest = Glide.with(context)
            .load(url)

        if (isCenterCrop) {
            glideRequest.centerCrop()
        }

        glideRequest.into(this)
    }
    companion object {
        const val DETAIL_STORY = "storycanary"
    }
}
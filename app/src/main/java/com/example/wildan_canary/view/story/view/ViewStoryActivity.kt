package com.example.wildan_canary.view.story.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wildan_canary.MainActivity
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivityViewStoryBinding
import com.example.wildan_canary.helper.ViewModelFactory
import com.example.wildan_canary.helper.adapter.AdapterStory
import com.example.wildan_canary.helper.adapter.LoadingState
import com.example.wildan_canary.helper.preference.Preferences
import com.example.wildan_canary.view.identity.MyIdentityActivity
import com.example.wildan_canary.view.sig.`in`.LoginActivity
import com.example.wildan_canary.view.story.add.AddStoryActivity
import com.example.wildan_canary.view.story.maps.MapsActivity
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class ViewStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewStoryBinding
    private lateinit var adapter: AdapterStory
    private var backPressedTime: Long = 0

    private val listStoryViewModel: ViewStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    companion object{
        fun start(context: Context) {
            val intent = Intent(context, ViewStoryActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE  // Show progress bar initially


        adapter = AdapterStory { story, imageView, nameView, descView ->
            // Handle callback if necessary
        }
        listStoryViewModel.stories.observe(this) { data ->
            binding.progressBar.visibility = View.GONE  // Hide progress bar when data is available
            if (data != null) {
                adapter.submitData(this.lifecycle, data)
            }
        }
        binding.swipeRefresh.setOnRefreshListener{
            SwipeRefresStory()
        }
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnAdd.setOnClickListener {
            AddStoryActivity.start(this)
        }
        binding.btnMaps.setOnClickListener {
            MapsActivity.start(this)
        }
        binding.btnAccount.setOnClickListener {
            toAccount()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity()
                } else {
                    Toast.makeText(this@ViewStoryActivity, getString(R.string.back_pressed), Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        setAdapter()
    }

    private fun setAdapter(){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvStories.layoutManager = layoutManager
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingState {
                adapter.retry()
            }
        )
        binding.rvStories.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }
    private fun SwipeRefresStory() {
        binding.swipeRefresh.isRefreshing = true
        adapter.refresh()
        Timer().schedule(2000) {
            binding.swipeRefresh.isRefreshing = false
            binding.rvStories.smoothScrollToPosition(0)
        }
    }
    private fun toAccount() {

        MyIdentityActivity.start(this)
    }
    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }
}
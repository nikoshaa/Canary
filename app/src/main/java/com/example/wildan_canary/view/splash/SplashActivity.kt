package com.example.wildan_canary.view.splash

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wildan_canary.MainActivity
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivitySplashBinding
import com.example.wildan_canary.helper.preference.Preferences
import com.example.wildan_canary.view.story.view.ViewStoryActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    companion object {
        private const val DURATION: Long = 1500
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.progresBar?.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        val sharedPref = Preferences.initPref(this, "onSignIn")
        val token = sharedPref.getString("token", "")
        if (token != "") {
            Handler(Looper.getMainLooper()).postDelayed({
                ViewStoryActivity.start(this)

            }, DURATION)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                MainActivity.start(this)
            }, DURATION)
        }
    }
}
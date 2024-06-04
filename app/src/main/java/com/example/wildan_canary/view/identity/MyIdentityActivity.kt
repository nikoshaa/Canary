package com.example.wildan_canary.view.identity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.wildan_canary.MainActivity
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivityMyIdentityBinding
import com.example.wildan_canary.helper.preference.Preferences
import com.example.wildan_canary.view.story.view.ViewStoryActivity
import kotlinx.coroutines.launch

class MyIdentityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyIdentityBinding
    companion object{
        fun start(context: Context) {
            val intent = Intent(context, MyIdentityActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            actionLogout.setOnClickListener {
                logout()
            }
        }
    }
    private fun logout() {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.message_logout_confirm))
            ?.setPositiveButton(getString(R.string.action_yes)) { _, _ ->
                lifecycleScope.launch {
                    Preferences.logOut(this@MyIdentityActivity)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
            ?.setNegativeButton(getString(R.string.action_cancel), null)
        val alert = alertDialog.create()
        alert.show()
    }
}
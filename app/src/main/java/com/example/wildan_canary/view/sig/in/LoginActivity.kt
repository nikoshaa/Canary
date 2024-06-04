package com.example.wildan_canary.view.sig.`in`

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.wildan_canary.MainActivity
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivityLoginBinding
import com.example.wildan_canary.helper.ViewModelFactory
import com.example.wildan_canary.data.network.response.Result
import com.example.wildan_canary.data.network.response.auth.sigin.InResponse
import com.example.wildan_canary.helper.preference.Preferences
import com.example.wildan_canary.view.story.view.ViewStoryActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.apply {
           btnLogin.setOnClickListener {
               takeData()
           }
       }


    }
    private fun takeData(){
        binding.apply {
            val email = edLoginEmail.editText?.text.toString()
            val password = edLoginPassword.editText?.text.toString()
            loginUser(email, password)
        }
    }
    private fun loginUser(email: String, password: String){
        loginViewModel.loginAccount(email, password).observe(this){result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        processLogin(result.data)
                        showLoading(false)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    private fun processLogin(data: InResponse) {
        if (data.error) {
            Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
        } else {
            Preferences.saveToken(data.loginResult.token, this)
            ViewStoryActivity.start(this)
        }
    }
    private fun showLoading(state : Boolean){
        binding.apply {
            progressBar.isVisible = state
        }
    }
}
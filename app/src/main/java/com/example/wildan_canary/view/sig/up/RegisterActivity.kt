package com.example.wildan_canary.view.sig.up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.wildan_canary.data.network.response.Result
import com.example.wildan_canary.data.network.response.auth.sigup.UpResponse
import com.example.wildan_canary.databinding.ActivityRegisterBinding
import com.example.wildan_canary.helper.ViewModelFactory
import com.example.wildan_canary.view.sig.`in`.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(this)
    }
    companion object{
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnRegister.setOnClickListener {
                takeData()
            }
        }


    }
    private fun takeData(){
        binding.apply {
            val username = edRegiterUsername.editText?.text.toString()
            val email = edRegiterEmail.editText?.text.toString()
            val password = edRegiterPassword.editText?.text.toString()
            registerUser(username, email, password)
        }
    }
    private fun registerUser(username: String, email: String, password: String){
        registerViewModel.registerAccount(username, email, password).observe(this){
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        processSignUp(it.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }
    private fun processSignUp(data: UpResponse) {
        if (data.error) {
            Toast.makeText(this, "Failed to Sign Up", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Sign Up was successful, please log in!", Toast.LENGTH_LONG).show()
            LoginActivity.start(this)
        }
    }
    private fun showLoading(state : Boolean){
        binding.apply {
            progressBar.isVisible = state
        }
    }
}
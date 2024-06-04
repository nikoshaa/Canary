package com.example.wildan_canary.view.sig.up

import androidx.lifecycle.ViewModel
import com.example.wildan_canary.helper.repository.UniversalRepository

class RegisterViewModel(private val storyRepository: UniversalRepository) : ViewModel() {
    fun registerAccount(name: String, email: String, password: String) = storyRepository.postSignUp(name, email, password)
}
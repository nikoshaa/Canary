package com.example.wildan_canary.view.sig.`in`

import androidx.lifecycle.ViewModel
import com.example.wildan_canary.helper.repository.UniversalRepository

class LoginViewModel(private val storyRepository: UniversalRepository) : ViewModel() {
    fun loginAccount(email: String, password: String) = storyRepository.postLogin(email, password)
}
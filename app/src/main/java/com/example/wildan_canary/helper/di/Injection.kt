package com.example.wildan_canary.helper.di

import android.content.Context
import com.example.wildan_canary.data.network.retrofit.ApiConfig
import com.example.wildan_canary.helper.repository.UniversalRepository

object Injection {
    fun provideRepository(context: Context): UniversalRepository {
        val apiService = ApiConfig.getApiService(context)
        return UniversalRepository(apiService)
    }
}
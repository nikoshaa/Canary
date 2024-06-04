package com.example.wildan_canary.helper.repository

import com.example.wildan_canary.data.network.retrofit.ApiService

class WidgetRepository (private val apiService: ApiService,
){

    suspend fun getStoryWidget() = apiService.getWidgetStories()

}
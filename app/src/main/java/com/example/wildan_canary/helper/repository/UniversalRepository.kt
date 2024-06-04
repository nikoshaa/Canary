package com.example.wildan_canary.helper.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.wildan_canary.data.network.paging.PagingStory
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.data.network.response.story.StoriesResponse
import com.example.wildan_canary.data.network.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.wildan_canary.data.network.response.Result
import com.example.wildan_canary.data.network.response.auth.sigin.InResponse
import com.example.wildan_canary.data.network.response.auth.sigup.UpResponse
import com.example.wildan_canary.data.network.response.story.AddStoryResponse
import com.example.wildan_canary.helper.preference.Preferences

class UniversalRepository(private val apiService: ApiService) {
    fun getStories(): LiveData<PagingData<ItemStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingStory(apiService)
            }
        ).liveData
    }
    suspend fun getStoryWidget() = apiService.getWidgetStories()

    fun getStoriesWithLocation(): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postStory(file: MultipartBody.Part, description: RequestBody): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
    suspend fun addStory(
        multipartBody: MultipartBody.Part,
        requestBodyDescription: RequestBody,
        latRequestBody: RequestBody?,
        lonRequestBody: RequestBody?
    ) = apiService.uploadImage(multipartBody, requestBodyDescription, latRequestBody, lonRequestBody)

    fun postSignUp(name: String, email: String, password: String): LiveData<Result<UpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postSignUp(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "postSignUp: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getDetailStory(id: String) = apiService.getDetailStory(id)

    fun postLogin(email: String, password: String): LiveData<Result<InResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "postLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

}
package com.example.wildan_canary.view.story.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wildan_canary.data.network.response.story.ResultResponse
import com.example.wildan_canary.helper.repository.UniversalRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import com.example.wildan_canary.data.network.response.Result
class AddStoryViewModel(
    private val storyRepository: UniversalRepository
): ViewModel() {
    private val _responseResult = MutableLiveData<Result<ResultResponse>>()
    val responseResult: MutableLiveData<Result<ResultResponse>> = _responseResult

    fun addStory(multipartBody: MultipartBody.Part, requestBodyDescription: RequestBody, latRequestBody: RequestBody?, lonRequestBody: RequestBody?){
        viewModelScope.launch {
            try {
                _responseResult.value = Result.Loading
                val response = storyRepository.addStory(multipartBody, requestBodyDescription,latRequestBody,lonRequestBody)
                _responseResult.value = Result.Success(response)
                _responseResult.value = Result.Error("Token not found")


            }catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ResultResponse::class.java)
                _responseResult.value = Result.Error(errorResponse.message)
            }

        }
    }

}
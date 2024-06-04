package com.example.wildan_canary.view.story.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.helper.repository.UniversalRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.wildan_canary.data.network.response.Result

class DetailStoryViewModel(
    private val storyRepository: UniversalRepository
): ViewModel(){
    private var _story = MutableLiveData<Result<ItemStory>>()
    val story = _story

    fun getDetailStory(id: String){
        viewModelScope.launch {
            try {
                _story.value = Result.Loading
                val response = storyRepository.getDetailStory(id)
                if (!response.error){
                    _story.value = Result.Success(response.story)
                }
            }catch (e: HttpException){
                _story.value = Result.Error(e.message())
            }
        }
    }
}
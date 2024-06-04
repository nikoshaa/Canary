package com.example.wildan_canary.view.story.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.helper.repository.UniversalRepository

class ViewStoryViewModel(storyRepository: UniversalRepository): ViewModel() {
    val stories: LiveData<PagingData<ItemStory>> = storyRepository.getStories().cachedIn(viewModelScope)
}
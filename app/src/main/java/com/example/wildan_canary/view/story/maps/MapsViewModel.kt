package com.example.wildan_canary.view.story.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.helper.repository.UniversalRepository
import com.google.android.gms.maps.model.LatLng

class MapsViewModel(private val storyRepository: UniversalRepository): ViewModel() {


    private val _storyLocation = MutableLiveData<Result<List<ItemStory>>>()
    val storyLocation: MutableLiveData<Result<List<ItemStory>>> = _storyLocation
    val coordinateTemp = MutableLiveData(LatLng(-2.3932797, 108.8507139))

    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}
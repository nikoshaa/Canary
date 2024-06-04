package com.example.wildan_canary.view.story.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ImageViewModel (private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val _selectedImage = savedStateHandle.getLiveData<Uri?>("selectedImage")
    val selectedImage: LiveData<Uri?> = _selectedImage

    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }
}
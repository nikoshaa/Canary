package com.example.wildan_canary.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wildan_canary.helper.di.Injection
import com.example.wildan_canary.view.sig.`in`.LoginViewModel
import com.example.wildan_canary.view.sig.up.RegisterViewModel
import com.example.wildan_canary.view.story.add.AddStoryViewModel
import com.example.wildan_canary.view.story.detail.DetailStoryViewModel
import com.example.wildan_canary.view.story.maps.MapsViewModel
import com.example.wildan_canary.view.story.view.ViewStoryViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(ViewStoryViewModel::class.java) -> {
                ViewStoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(Injection.provideRepository(context)) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
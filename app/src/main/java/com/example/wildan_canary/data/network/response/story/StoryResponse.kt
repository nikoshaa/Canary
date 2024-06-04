package com.example.wildan_canary.data.network.response.story

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<ItemStory>,
    @SerializedName("message")
    val message: String,
)
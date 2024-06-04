package com.example.wildan_canary.data.network.response.story

import com.google.gson.annotations.SerializedName

data class ResultResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

package com.example.wildan_canary.data.network.response.auth.sigin

import com.example.wildan_canary.data.network.response.auth.sigin.InResult
import com.google.gson.annotations.SerializedName

data class InResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: InResult,
    @SerializedName("message")
    val message: String
)
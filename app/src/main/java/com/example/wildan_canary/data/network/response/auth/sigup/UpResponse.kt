package com.example.wildan_canary.data.network.response.auth.sigup

import com.google.gson.annotations.SerializedName

data class UpResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)
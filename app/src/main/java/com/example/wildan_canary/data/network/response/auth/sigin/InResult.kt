package com.example.wildan_canary.data.network.response.auth.sigin

import com.google.gson.annotations.SerializedName

data class InResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String
)
package com.example.wildan_canary.data.network.response.story

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class ItemStory(
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("description")
    val description: String,
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("lat")
    val lat: Double,
    @field:SerializedName("lon")
    val lon: Double,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("photoUrl")
    val photoUrl: String
)
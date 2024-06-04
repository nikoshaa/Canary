package com.example.wildan_canary.data.network.retrofit

import com.example.wildan_canary.data.network.response.auth.sigin.InResponse
import com.example.wildan_canary.data.network.response.auth.sigup.UpResponse
import com.example.wildan_canary.data.network.response.story.AddStoryResponse
import com.example.wildan_canary.data.network.response.story.ResultResponse
import com.example.wildan_canary.data.network.response.story.StoriesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): UpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): InResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int,
    ) : StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): StoriesResponse
    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): ResultResponse
    @GET("stories")
    suspend fun getWidgetStories():StoriesResponse
}
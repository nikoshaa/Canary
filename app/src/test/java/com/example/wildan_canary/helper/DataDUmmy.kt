package com.example.wildan_canary.helper

import com.example.wildan_canary.data.network.response.auth.sigin.InResponse
import com.example.wildan_canary.data.network.response.auth.sigin.InResult
import com.example.wildan_canary.data.network.response.auth.sigup.UpResponse
import com.example.wildan_canary.data.network.response.story.ItemStory
import com.example.wildan_canary.data.network.response.story.StoriesResponse
import com.example.wildan_canary.data.network.response.story.StoryResponse

object DataDUmmy {
    fun generateDummyRegister(): UpResponse {
        return UpResponse(
            error = false,
            message = "User Created"
        )
    }
    fun generateDummyStories(): StoryResponse {
        val listStory = ArrayList<ItemStory>()
        for (i in 1..20) {
            val story = ItemStory(
                createdAt = "2022-02-22T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650"
            )
            listStory.add(story)
        }

        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }
    fun generateDummyLogin(): InResponse {
        return InResponse(
            error = false,
            message = "success",
            loginResult = InResult(
                userId = "user-yj5pc_LARC_AgK61",
                name = "Arif Faizin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
            )
        )
    }
}
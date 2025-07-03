package com.example.manganese.api

import com.example.manganese.database.entities.animeX
import com.example.manganese.database.entities.mangaX
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface bkendService {

    @POST("/recommend")
    suspend fun getRecommendationAnime(@Body watchList: List<String>):Response<List<Int>>

    @GET("/trendingManga")
    suspend fun getTrendingMangas():Response<mangaX>

    @GET("/trendingAnime")
    suspend fun getTrendingAnimes():Response<animeX>
}
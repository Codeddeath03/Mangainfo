package com.example.manganese.api

import com.example.manganese.database.entities.animeX
import com.example.manganese.database.entities.mangaX
import retrofit2.Response
import retrofit2.http.GET

interface bkendService {
    @GET("/trendingManga")
    suspend fun getTrendingMangas():Response<mangaX>

    @GET("/trendingAnime")
    suspend fun getTrendingAnimes():Response<animeX>
}
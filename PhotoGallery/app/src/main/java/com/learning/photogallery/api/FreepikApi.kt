package com.learning.photogallery.api

import com.learning.photogallery.FreepikSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FreepikApi {
    @GET("v1/resources")
    suspend fun searchPhotos(
        @Query("term") query: String,           // поисковая строка, например "cat"
        @Query("page") page: Int,               // страница (начиная с 1)
        @Query("limit") limit: Int = 30       // размер страницы (до 150)
    ): FreepikSearchResponse
}
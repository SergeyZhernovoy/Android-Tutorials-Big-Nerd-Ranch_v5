package com.learning.photogallery.api

import retrofit2.http.GET

interface FlickrApi {

    @GET("/")
    suspend fun fetchContent(): String

}
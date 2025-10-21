package com.learning.photogallery.api

import com.learning.photogallery.OpenVerseResponse
import com.learning.photogallery.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OpenVerseApi {

    @GET("v1/images")
    suspend fun fetchPhotos(@Header("Authorization") authToken: String,
                             @Query("q") search: String = "dog+cat"): OpenVerseResponse

    @FormUrlEncoded
    @POST("v1/auth_tokens/token/")
    suspend fun token(
                @Field("grant_type") grantType: String,
                @Field("client_id") clientId: String,
                @Field("client_secret") clientSecret: String): TokenResponse

}
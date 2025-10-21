package com.learning.photogallery

import com.learning.photogallery.api.OpenVerseApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class PhotoRepository {
    private val openVerseApi: OpenVerseApi

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openverse.org")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        openVerseApi = retrofit.create<OpenVerseApi>()
    }

    suspend fun fetchContent(apiKey: String): List<GalleryItem> {
        return openVerseApi
            .fetchPhotos(authToken ="Bearer $apiKey", search = "naked woman")
            .galleryItems
    }

    suspend fun token(): String {
        return openVerseApi.token(
            "client_credentials",
            BuildConfig.CLIENT_ID,
            BuildConfig.CLIENT_SECRET)
            .accessToken
    }

}
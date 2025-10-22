package com.learning.photogallery

import com.learning.photogallery.api.FreepikApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


class PhotoRepository {
    private val freepikApi: FreepikApi

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-freepik-api-key", BuildConfig.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.freepik.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        freepikApi = retrofit.create<FreepikApi>()
    }

    suspend fun fetchContentPaged(page: Int, pageSize: Int, query: String): List<FreepikImage> {
        // Запрос только фото!
        return freepikApi
            .searchPhotos(query = query, page = page, limit = pageSize)
            .data
    }
}
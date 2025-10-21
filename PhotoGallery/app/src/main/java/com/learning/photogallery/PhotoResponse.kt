package com.learning.photogallery

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenVerseResponse(
    @field:Json(name = "results")
    val galleryItems: List<GalleryItem>
)

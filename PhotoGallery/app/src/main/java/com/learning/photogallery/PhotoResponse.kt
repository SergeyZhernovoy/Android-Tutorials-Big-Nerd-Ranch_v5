package com.learning.photogallery

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FreepikSearchResponse(
    @Json(name = "data") val data: List<FreepikImage>,
    @Json(name = "meta") val meta: FreepikMeta
)

@JsonClass(generateAdapter = true)
data class FreepikImage(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "filename") val filename: String?,
    @Json(name = "licenses") val licenses: List<FreepikLicense>?,
    @Json(name = "products") val products: List<FreepikProduct>?,
    @Json(name = "meta") val meta: FreepikMetaInfo?,
    @Json(name = "image") val image: FreepikImageInfo?,
    @Json(name = "related") val related: FreepikRelated?,
    @Json(name = "stats") val stats: FreepikStats?,
    @Json(name = "author") val author: FreepikAuthor?
) {
    val photoPageUri: Uri
        get() = image?.source?.url?.toUri() ?: Uri.EMPTY
            .buildUpon()
            .appendPath(id.toString())
            .build()
}

@JsonClass(generateAdapter = true)
data class FreepikLicense(
    @Json(name = "type") val type: String?,
    @Json(name = "url") val url: String?
)

@JsonClass(generateAdapter = true)
data class FreepikProduct(
    @Json(name = "type") val type: String?,
    @Json(name = "url") val url: String?
)

@JsonClass(generateAdapter = true)
data class FreepikMetaInfo(
    @Json(name = "published_at") val publishedAt: String?,
    @Json(name = "is_new") val isNew: Boolean?,
    @Json(name = "available_formats") val availableFormats: Map<String, FreepikAvailableFormat>?
)

@JsonClass(generateAdapter = true)
data class FreepikAvailableFormat(
    @Json(name = "total") val total: Int?,
    @Json(name = "items") val items: List<FreepikFormatItem>?
)

@JsonClass(generateAdapter = true)
data class FreepikFormatItem(
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String?,
    @Json(name = "colorspace") val colorspace: String?,
    @Json(name = "size") val size: Int?
)

@JsonClass(generateAdapter = true)
data class FreepikImageInfo(
    @Json(name = "type") val type: String?,
    @Json(name = "orientation") val orientation: String?,
    @Json(name = "source") val source: FreepikImageSource?
)

@JsonClass(generateAdapter = true)
data class FreepikImageSource(
    @Json(name = "url") val url: String?,
    @Json(name = "key") val key: String?,
    @Json(name = "size") val size: String?
)

@JsonClass(generateAdapter = true)
data class FreepikRelated(
    @Json(name = "serie") val serie: List<Any>?,
    @Json(name = "others") val others: List<Any>?,
    @Json(name = "keywords") val keywords: List<Any>?
)

@JsonClass(generateAdapter = true)
data class FreepikStats(
    @Json(name = "downloads") val downloads: Int?,
    @Json(name = "likes") val likes: Int?
)

@JsonClass(generateAdapter = true)
data class FreepikAuthor(
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String?,
    @Json(name = "avatar") val avatar: String?,
    @Json(name = "slug") val slug: String?
)

@JsonClass(generateAdapter = true)
data class FreepikMeta(
    @Json(name = "current_page") val currentPage: Int,
    @Json(name = "last_page") val lastPage: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "total") val total: Int
)

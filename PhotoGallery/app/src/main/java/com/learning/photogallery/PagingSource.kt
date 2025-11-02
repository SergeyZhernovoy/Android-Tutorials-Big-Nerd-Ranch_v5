package com.learning.photogallery

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PhotoPagingSource(
    private val repository: PhotoRepository,
    private val query: String
) : PagingSource<Int, FreepikImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FreepikImage> {
        val page = params.key ?: 1
        Log.d("PhotoPagingSource", "loading page: $page")
        try {
            val items = repository.fetchContentPaged(page, params.loadSize, query = query)
            Log.d("PhotoPagingSource", "items.size: ${items.size}")
            return LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PhotoPagingSource", "Ошибка загрузки страницы: $page", e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FreepikImage>): Int? = 1
}

package com.learning.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository =  PhotoRepository()

    val pagingDataFlow: Flow<PagingData<FreepikImage>> = kotlinx.coroutines.flow.flow {
        emitAll(
            Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { PhotoPagingSource(photoRepository) }
            ).flow.cachedIn(viewModelScope)
        )
    }

}
package com.learning.photogallery

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class PhotoGalleryUiState(
    val query: String,
    val isPolling: Boolean = false
)
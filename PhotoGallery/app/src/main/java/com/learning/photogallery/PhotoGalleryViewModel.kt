package com.learning.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"
private const val DEFAULT_QUERY = "cat"

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository =  PhotoRepository()
    private val preferencesRepository = PreferencesRepository.get()

    private val queryState = MutableStateFlow("")
    private val pollingState = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { q ->
                queryState.value = q.ifBlank { DEFAULT_QUERY }
            }
        }
        viewModelScope.launch {
            preferencesRepository.isPolling.collect { isPolling ->
                pollingState.value = isPolling
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PhotoGalleryUiState> =
            combine(queryState, pollingState) { query, isPolling ->
                PhotoGalleryUiState(
                    query = query.ifBlank { DEFAULT_QUERY },
                    isPolling = isPolling
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PhotoGalleryUiState(
                    query = DEFAULT_QUERY,
                    isPolling = false
                )
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<FreepikImage>> = queryState
        .flatMapLatest { query ->
            pagerFlowFor(query.ifBlank { DEFAULT_QUERY })
        }
        .cachedIn(viewModelScope)

    private fun pagerFlowFor(q: String): Flow<PagingData<FreepikImage>> =
        Pager(
            initialKey = 1,
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { PhotoPagingSource(photoRepository, q) }
        ).flow

    fun setQuery(query: String) {
        val normalized = query.ifBlank { DEFAULT_QUERY }
        queryState.value = normalized
        viewModelScope.launch {
            preferencesRepository.setStoredQuery(normalized)
        }
    }

    fun togglePolling() {
        viewModelScope.launch {
            preferencesRepository.setPolling(!uiState.value.isPolling)
        }
    }

}
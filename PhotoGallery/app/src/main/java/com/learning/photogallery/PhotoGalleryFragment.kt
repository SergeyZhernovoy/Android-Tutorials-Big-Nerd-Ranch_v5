package com.learning.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.learning.photogallery.databinding.FragmentPhotoGalleryBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "PhotoGalleryFragment"
private const val POLL_WORK = "POLL_WORK"

/**
 * A fragment representing a list of Items.
 */
class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible ?"
        }

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()

    private val adapter = PhotoPagingAdapter()

    private var searchView: SearchView? = null
    private var pollingMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.fragment_photo_gallery, menu)
                val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
                pollingMenuItem = menu.findItem(R.id.menu_item_toggle_polling)
                searchView = searchItem.actionView as? SearchView
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Log.d(TAG, "QueryTextSubmit: $query")
                        photoGalleryViewModel.setQuery(query ?: "")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d(TAG, "QueryTextChange: $newText")
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
               return when (menuItem.itemId) {
                   R.id.menu_item_clear -> {
                       photoGalleryViewModel.setQuery("")
                       true
                   }
                   R.id.menu_item_toggle_polling -> {
                       photoGalleryViewModel.togglePolling()
                       true
                   }
                   else -> false
               }
            }
        })
        return binding.root
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photoGrid.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    photoGalleryViewModel.uiState.collect { state ->
                        if (searchView?.query?.toString() != state.query) {
                            searchView?.setQuery(state.query, false)
                        }
                        updatePollingState(state.isPolling)
                    }
                }
                launch {
                    photoGalleryViewModel.pagingDataFlow.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            when (val refresh = loadState.refresh) {
                is androidx.paging.LoadState.Loading -> Log.d(TAG, "refresh: loading")
                is androidx.paging.LoadState.NotLoading -> {
                    Log.d(TAG, "refresh: success; items=${adapter.itemCount}")
                    if (adapter.itemCount > 0) binding.photoGrid.scrollToPosition(0)
                }
                is androidx.paging.LoadState.Error -> {
                    Log.e(TAG, "refresh: error", refresh.error)
                }
            }
        }
    }

    private fun updatePollingState(isPolling: Boolean) {
       val toggleItemTitle = if (isPolling) {
           R.string.stop_polling
       } else {
           R.string.start_polling
       }
        pollingMenuItem?.setTitle(toggleItemTitle)

        if (isPolling) {
            val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
            val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        } else {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView = null
        pollingMenuItem = null
        _binding = null
    }
}
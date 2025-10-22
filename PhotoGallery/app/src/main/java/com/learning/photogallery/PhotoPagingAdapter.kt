package com.learning.photogallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.learning.photogallery.databinding.ListItemGalleryBinding

class PhotoPagingAdapter :
    PagingDataAdapter<FreepikImage, PhotoViewHolder>(GalleryItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}

class GalleryItemDiffCallback : DiffUtil.ItemCallback<FreepikImage>() {

    override fun areItemsTheSame(old: FreepikImage, new: FreepikImage): Boolean =
        old.id == new.id

    override fun areContentsTheSame(old: FreepikImage, new: FreepikImage): Boolean =
        old == new

}

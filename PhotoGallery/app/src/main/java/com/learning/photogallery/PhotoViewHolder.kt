package com.learning.photogallery

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.learning.photogallery.databinding.ListItemGalleryBinding

class PhotoViewHolder(
    private val binding: ListItemGalleryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryItem: FreepikImage) {
        val url = galleryItem.image?.source?.url
        binding.itemImageView.load(url) {
            placeholder(R.drawable.bill_up_close)
        }
    }

}
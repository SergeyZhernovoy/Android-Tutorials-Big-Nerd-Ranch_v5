package com.learning.photogallery

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.learning.photogallery.databinding.ListItemGalleryBinding

class PhotoViewHolder(
    private val binding: ListItemGalleryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryItem: FreepikImage, onItemClicked: (Uri) -> Unit) {
        val url = galleryItem.image?.source?.url
        binding.itemImageView.load(url) {
            placeholder(R.drawable.bill_up_close)
        }
        binding.root.setOnClickListener {
            onItemClicked(galleryItem.photoPageUri)
        }
    }

}
package com.learning.photogallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learning.photogallery.databinding.ListItemGalleryBinding

class PhotoListAdapter(
    private val galleryItems: List<FreepikImage>,
    private val onItemClicked: (Uri) -> Unit
) : RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PhotoViewHolder,
        position: Int
    ) {
        val item = galleryItems[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int = galleryItems.size
}
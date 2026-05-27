package com.laxman.klinqpdp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.laxman.klinqpdp.databinding.ItemProductImageBinding
import com.laxman.klinqpdp.ui.adapter.diff.ProductImageDiffCallback

class ProductImageAdapter :
    ListAdapter<String, ProductImageAdapter.ProductImageViewHolder>(
        ProductImageDiffCallback()
    ) {

    inner class ProductImageViewHolder(
        private val binding: ItemProductImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            binding.ivProductImage.load(imageUrl)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImageViewHolder {
        val binding = ItemProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductImageViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    fun submitImageList(images: List<String>) {
        submitList(images)
    }
}
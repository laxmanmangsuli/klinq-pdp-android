package com.laxman.klinqpdp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.laxman.klinqpdp.R
import com.laxman.klinqpdp.data.model.ColorVariant
import com.laxman.klinqpdp.databinding.ItemColorVariantBinding
import com.laxman.klinqpdp.ui.adapter.diff.ColorVariantDiffCallback

class ColorVariantAdapter(
    private val onColorClick: (ColorVariant) -> Unit
) : ListAdapter<ColorVariant, ColorVariantAdapter.ColorViewHolder>(
    ColorVariantDiffCallback()
) {

    private var selectedPosition = -1

    inner class ColorViewHolder(
        private val binding: ItemColorVariantBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            colorVariant: ColorVariant,
            position: Int
        ) {
            binding.ivColorVariant.load(colorVariant.swatchUrl)

            if (position == selectedPosition) {
                binding.cardVariant.strokeWidth = 2
                binding.cardVariant.strokeColor =
                    binding.root.context.getColor(android.R.color.black)
            } else {
                binding.cardVariant.strokeWidth = 1
                binding.cardVariant.strokeColor =
                    binding.root.context.getColor(R.color.light_gray)
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onColorClick(colorVariant)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorViewHolder {
        val binding = ItemColorVariantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ColorViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), position)
    }

    fun submitColorList(colors: List<ColorVariant>) {
        selectedPosition = -1
        submitList(colors)

    }
}
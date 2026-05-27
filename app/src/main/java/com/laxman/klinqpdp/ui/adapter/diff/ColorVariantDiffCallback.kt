package com.laxman.klinqpdp.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.laxman.klinqpdp.data.model.ColorVariant

class ColorVariantDiffCallback : DiffUtil.ItemCallback<ColorVariant>() {

    override fun areItemsTheSame(
        oldItem: ColorVariant,
        newItem: ColorVariant
    ): Boolean {
        return oldItem.optionId == newItem.optionId
    }

    override fun areContentsTheSame(
        oldItem: ColorVariant,
        newItem: ColorVariant
    ): Boolean {
        return oldItem == newItem
    }
}
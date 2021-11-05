package com.shopping.utils.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class UtilsRecyclerViewDiffUtil<Item> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) =
        getId(oldItem as Any) == getId(newItem as Any)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

    private fun getId(x: Any) = x::class.java.getMethod("getId").invoke(x) as Int
}
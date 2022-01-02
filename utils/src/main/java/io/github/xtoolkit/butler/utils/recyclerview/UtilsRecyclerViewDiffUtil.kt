package io.github.xtoolkit.butler.utils.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

@SuppressLint("DiffUtilEquals")
class UtilsRecyclerViewDiffUtil<Item> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem

//    private fun getId(x: Any) = x::class.java.getMethod("getId").invoke(x) as Int
}
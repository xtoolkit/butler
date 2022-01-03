package io.github.xtoolkit.butler.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter

class EasyAdapter<Binding : ViewDataBinding, Item>(
    private val layout: (LayoutInflater, ViewGroup?, Boolean) -> Binding,
    private val bind: Binding.(Item) -> Unit
) : ListAdapter<Item, UtilsRecyclerViewHolder<Binding>>(UtilsRecyclerViewDiffUtil<Item>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UtilsRecyclerViewHolder(layout(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UtilsRecyclerViewHolder<Binding>, position: Int) {
        bind(holder.binding, getItem(position))
    }
}


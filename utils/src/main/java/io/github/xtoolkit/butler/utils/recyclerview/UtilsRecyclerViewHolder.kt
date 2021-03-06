package io.github.xtoolkit.butler.utils.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class UtilsRecyclerViewHolder<Binding : ViewDataBinding>(val binding: Binding) :
    RecyclerView.ViewHolder(binding.root)
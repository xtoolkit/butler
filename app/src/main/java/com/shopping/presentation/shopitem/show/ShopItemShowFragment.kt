package com.shopping.presentation.shopitem.show

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.shopping.databinding.FragmentShopitemShowBinding
import com.shopping.databinding.ItemShopitemBinding
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.EDIT_MODE_CHANGED
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.SHOW_ALERT
import com.shopping.presentation.shopitem.show.converter.toDomain
import com.shopping.utils.recyclerview.EasyAdapter
import com.shopping.utils.snackbar.SnackBarModel
import com.shopping.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemShowFragment : Fragment() {
    private var binding: FragmentShopitemShowBinding? = null
    private val viewModel: ShopItemShowFragmentViewModel by viewModel()
    private val isLandscape by lazy { resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    private var shopListId = -1
    private val adapter = EasyAdapter(ItemShopitemBinding::inflate) { it: ShopItemShowUIItem ->
        shopitem = it.toDomain()
        isEdit = viewModel.isEdit
        input.setText(it.quantity.toString())
        background.setOnClickListener { _ -> viewModel.toggleDone(it) }
        remove.setOnClickListener { _ -> viewModel.changeQuantity(it, it.quantity - 1) }
        add.setOnClickListener { _ -> viewModel.changeQuantity(it, it.quantity + 1) }
    }
    private val domain
        get() = ShopItemShowUIItem(
            viewModel.items.value.size,
            binding!!.input.text.toString(),
            isNew = true
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopitemShowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = requireNotNull(binding)

        binding.isEdit = viewModel.isEdit

        binding.list.adapter = adapter
        binding.list.layoutManager = GridLayoutManager(requireContext(), if (isLandscape) 5 else 3)

        lifecycleScope.launchWhenResumed { viewModel.shopList.collect { binding.shoplist = it } }

        lifecycleScope.launchWhenResumed { viewModel.items.collect { adapter.submitList(it) } }

        viewModel.on(SHOW_ALERT) { it: SnackBarModel -> snackBarBuilder(binding.list, it) }

        viewModel.on(EDIT_MODE_CHANGED) {
            binding.isEdit = viewModel.isEdit
            adapter.notifyItemRangeChanged(0, viewModel.items.value.size)
            binding.switchLayout.box.transitionToStart()
        }

        binding.menu.setOnClickListener {
            parentFragmentManager.setFragmentResult("openShopListMenu", bundleOf())
        }

        parentFragmentManager
            .setFragmentResultListener("shopListItemsChanged", viewLifecycleOwner) { _, it ->
                shopListId = it.getInt("id")
                viewModel.changeShopList(shopListId)
            }

        binding.edit.setOnClickListener { viewModel.toggleEdit() }

        binding.submit.setOnClickListener { viewModel.addShopItem(domain) }

        binding.more.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "requestOpenShopListSetting",
                bundleOf("id" to shopListId)
            )
        }

        binding.switchLayout.btn1.setOnClickListener {
            binding.switchLayout.box.transitionToStart()
            viewModel.changeShopItemsShow(false)
        }
        binding.switchLayout.btn2.setOnClickListener {
            binding.switchLayout.box.transitionToEnd()
            viewModel.changeShopItemsShow(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
        parentFragmentManager.clearFragmentResultListener("shopListItemsChanged")
    }
}
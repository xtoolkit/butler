package com.shopping.presentation.shopitem.show

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.shopping.core.domain.ShopItem
import com.shopping.databinding.FragmentShopitemShowBinding
import com.shopping.databinding.ItemShopitemBinding
import com.shopping.presentation.shopitem.insert.ShopItemInsertActivity
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.SHOW_ALERT
import com.shopping.utils.recyclerview.EasyAdapter
import com.shopping.utils.snackbar.SnackBarModel
import com.shopping.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemShowFragment : Fragment() {
    private var binding: FragmentShopitemShowBinding? = null
    private val viewModel: ShopItemShowFragmentViewModel by viewModel()
    private val isLandscape by lazy { resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    private var shoplistId = -1
    private val adapter = EasyAdapter(ItemShopitemBinding::inflate) { it: ShopItem ->
        shopitem = it
        root.setOnClickListener { _ -> viewModel.toggleDone(it) }
    }

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

        binding.list.adapter = adapter
        binding.list.layoutManager = GridLayoutManager(requireContext(), if (isLandscape) 5 else 3)

        lifecycleScope.launchWhenResumed { viewModel.shopList.collect { binding.shoplist = it } }

        lifecycleScope.launchWhenResumed {
            viewModel.items.collect {
                adapter.submitList(it)
                binding.items = it.size
            }
        }

        viewModel.on(SHOW_ALERT) { it: SnackBarModel -> snackBarBuilder(binding.list, it) }

        binding.menu.setOnClickListener {
            parentFragmentManager.setFragmentResult("openShopListMenu", bundleOf())
        }

        parentFragmentManager.setFragmentResultListener("shopListChanged", this) { _, it ->
            shoplistId = it.getInt("id")
            viewModel.changeShopList(shoplistId)
        }

        binding.add.setOnClickListener {
            startActivity(
                Intent(requireContext(), ShopItemInsertActivity::class.java)
                    .putExtra("shoplistId", shoplistId)
            )
        }

        binding.switchLayout.btn1.setOnClickListener { binding.switchLayout.box.transitionToStart() }
        binding.switchLayout.btn2.setOnClickListener { binding.switchLayout.box.transitionToEnd() }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
        parentFragmentManager.clearFragmentResultListener("shopListChanged")
    }
}
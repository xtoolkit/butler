package com.shopping.presentation.shoplist.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shopping.core.domain.ShopList
import com.shopping.databinding.FragmentShoplistShowBinding
import com.shopping.databinding.ItemShoplistBinding
import com.shopping.presentation.shoplist.insert.ShopListInsertFragment
import com.shopping.presentation.shoplist.show.ShopListShowEvents.REQUEST_CHANGE_SHOP_LIST
import com.shopping.utils.recyclerview.EasyAdapter
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopListShowFragment : Fragment() {
    private var binding: FragmentShoplistShowBinding? = null
    private val viewModel: ShopListShowFragmentViewModel by viewModel()
    private val adapter = EasyAdapter(ItemShoplistBinding::inflate) { item: ShopListShowUIItem ->
        shoplist = item
        box.setOnClickListener { viewModel.selectShopList(item) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.start()
        lifecycleScope.launchWhenResumed { viewModel.list.collect { adapter.submitList(it) } }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoplistShowBinding.inflate(inflater, container, false)
        binding?.list?.adapter = adapter
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = requireNotNull(binding)

        binding.panelBottomPanel.btn.setOnClickListener {
            get<ShopListInsertFragment>().show(childFragmentManager, null)
        }

        binding.themeSwitch.setOnClickListener {
            parentFragmentManager.setFragmentResult("onSelectThemeSwitch", bundleOf())
        }

        viewModel.on(REQUEST_CHANGE_SHOP_LIST) { it: ShopList ->
            parentFragmentManager
                .setFragmentResult("onSelectedShopListChange", bundleOf("id" to it.id))
        }

        parentFragmentManager
            .setFragmentResultListener("shopListChanged", viewLifecycleOwner) { _, it ->
                viewModel.selectShopList(ShopListShowUIItem(it.getInt("id"), ""), true)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
        parentFragmentManager.clearFragmentResultListener("shopListChanged")
    }
}
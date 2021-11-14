package com.shopping.presentation.shoplist.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shopping.databinding.FragmentShoplistManageBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopListManageFragment : Fragment() {
    private var binding: FragmentShoplistManageBinding? = null
    private val viewModel: ShopListManageFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoplistManageBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = requireNotNull(binding)

        lifecycleScope.launchWhenResumed { viewModel.shopList.collect { binding.shoplist = it } }

        parentFragmentManager
            .setFragmentResultListener("shopListManageChanged", viewLifecycleOwner) { _, it ->
                viewModel.changeShopList(it.getInt("id"))
            }
    }
}
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
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.*
import com.shopping.presentation.shopitem.show.converter.toDomain
import com.shopping.utils.modalalert.ModalAlertModel
import com.shopping.utils.modalalert.modalAlertBuilder
import com.shopping.utils.recyclerview.EasyAdapter
import com.shopping.utils.snackbar.SnackBarModel
import com.shopping.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemShowFragment : Fragment() {
    private var binding: FragmentShopitemShowBinding? = null
    private val viewModel: ShopItemShowFragmentViewModel by viewModel()
    private val isLandscape by lazy { resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
    private val adapter = EasyAdapter(ItemShopitemBinding::inflate) { it: ShopItemShowUIItem ->
        shopitem = it.toDomain()
        isEdit = viewModel.isEdit
        input.setText(it.quantity.toString())
        background.setOnClickListener { _ ->
            input.clearFocus()
            viewModel.requestToggleDone(it)
        }
        add.setOnClickListener { _ ->
            input.clearFocus()
            it.quantity++
            input.setText(it.quantity.toString())
        }
        remove.setOnClickListener { _ ->
            input.clearFocus()
            if (it.quantity > 1) {
                it.quantity--
                input.setText(it.quantity.toString())
            } else viewModel.requestDeleteShopItem(it)
        }
        input.setOnKeyListener { _, _, _ ->
            val data = input.text.toString()
            it.quantity = if (data.isEmpty()) 1 else data.toInt()
            false
        }
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

        viewModel.on(SHOW_MODAL) { it: ModalAlertModel -> modalAlertBuilder(requireContext(), it) }

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
                viewModel.changeShopList(it.getInt("id"))
            }

        binding.edit.setOnClickListener {
            binding.list.requestFocus()
            viewModel.toggleEdit()
        }

        binding.submit.setOnClickListener { viewModel.requestAddShopItem(domain) }

        binding.more.setOnClickListener {
            parentFragmentManager.setFragmentResult("requestOpenShopListSetting", bundleOf())
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
package io.github.xtoolkit.butler.shopping.presentation.shopitem.show

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.xtoolkit.butler.shopping.databinding.FragmentShopitemShowBinding
import io.github.xtoolkit.butler.shopping.databinding.ItemShopitemBinding
import io.github.xtoolkit.butler.shopping.presentation.shopitem.show.ShopItemShowEvents.*
import io.github.xtoolkit.butler.shopping.presentation.shopitem.show.converter.toDomain
import io.github.xtoolkit.butler.utils.modalalert.ModalAlertModel
import io.github.xtoolkit.butler.utils.modalalert.modalAlertBuilder
import io.github.xtoolkit.butler.utils.recyclerview.EasyAdapter
import io.github.xtoolkit.butler.utils.snackbar.SnackBarModel
import io.github.xtoolkit.butler.utils.snackbar.snackBarBuilder
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
        val changeQuality = fun(newQuality: Int, isInput: Boolean) {
            if (!isInput) input.clearFocus()
            if (newQuality > 0) {
                it.quantity = newQuality
                shopitem = it.toDomain()
                viewModel.requestChangeQuantity(it, newQuality)
                if (!isInput) input.setText(newQuality.toString())
            } else viewModel.requestDeleteShopItem(it)
        }
        background.setOnClickListener { _ -> input.clearFocus(); viewModel.requestToggleDone(it) }
        add.setOnClickListener { _ -> changeQuality(it.quantity + 1, false) }
        remove.setOnClickListener { _ -> changeQuality(it.quantity - 1, false) }
        input.setOnKeyListener { _, _, _ ->
            val data = input.text.toString()
            changeQuality(if (data.isEmpty()) 1 else data.toInt(), true)
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

        lifecycleScope.launchWhenResumed { viewModel.shopList.collect { binding.shopList = it } }

        lifecycleScope.launchWhenResumed { viewModel.items.collect { adapter.submitList(it) } }

        viewModel.on(SHOW_ALERT) { it: SnackBarModel -> snackBarBuilder(binding.list, it) }

        viewModel.on(SHOW_MODAL) { it: ModalAlertModel -> modalAlertBuilder(requireContext(), it) }

        viewModel.on(CREATE_ITEM) { binding.input.setText("") }

        viewModel.on(EDIT_MODE_CHANGED) {
            binding.isEdit = viewModel.isEdit
            adapter.notifyItemRangeChanged(0, viewModel.items.value.size)
            binding.switchLayout.box.transitionToStart()
            parentFragmentManager.setFragmentResult(
                "requestLockPanel", bundleOf("lock" to viewModel.isEdit)
            )
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

        binding.save.setOnClickListener {
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

        binding.back.setOnClickListener {
            viewModel.requestBackToUnEdit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
        parentFragmentManager.clearFragmentResultListener("shopListItemsChanged")
    }
}
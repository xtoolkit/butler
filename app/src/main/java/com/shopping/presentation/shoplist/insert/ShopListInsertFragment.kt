package com.shopping.presentation.shoplist.insert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopping.core.domain.ShopList
import com.shopping.databinding.FragmentShoplistInsertBinding
import com.shopping.presentation.shoplist.insert.ShopListInsertEvents.DISMISS_FRAGMENT
import com.shopping.presentation.shoplist.insert.ShopListInsertEvents.NAME_VALIDITY
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopListInsertFragment : BottomSheetDialogFragment() {
    private var binding: FragmentShoplistInsertBinding? = null
    private val viewModel: ShopListInsertFragmentViewModel by viewModel()
    private val domain: ShopList
        get() {
            val binding = requireNotNull(binding)
            val description = binding.listDescription.editText!!.text.toString()
            return ShopList(
                0,
                binding.listName.editText!!.text.toString(),
                if (description.isBlank()) null else description
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoplistInsertBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = requireNotNull(binding)

        viewModel.on(NAME_VALIDITY) { it: String? -> binding.listName.error = it }
        viewModel.once(DISMISS_FRAGMENT) { dismiss() }

        binding.submit.setOnClickListener { viewModel.addShopList(domain) }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }
}
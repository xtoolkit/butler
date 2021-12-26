package io.github.xtoolkit.butler.shopping.presentation.shoplist.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.databinding.FragmentShoplistManageBinding
import io.github.xtoolkit.butler.shopping.presentation.shoplist.manage.ShopListManageEvents.*
import io.github.xtoolkit.butler.utils.modalalert.ModalAlertModel
import io.github.xtoolkit.butler.utils.modalalert.modalAlertBuilder
import io.github.xtoolkit.butler.utils.snackbar.SnackBarModel
import io.github.xtoolkit.butler.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopListManageFragment : Fragment() {
    private var binding: FragmentShoplistManageBinding? = null
    private val viewModel: ShopListManageFragmentViewModel by viewModel()
    private val domain: ShopList
        get() {
            val binding = requireNotNull(binding)
            val description = binding.listDescription.editText!!.text.toString()
            return ShopList(
                viewModel.shopList.value!!.id,
                binding.listName.editText!!.text.toString(),
                if (description.isEmpty()) null else description
            )
        }

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

        viewModel.on(NAME_ERROR) { it: String? -> binding.listName.error = it }

        viewModel.on(SHOW_ALERT) { it: SnackBarModel ->
            snackBarBuilder(binding.panelBottomPanel.btn, it)
        }

        viewModel.on(SHOW_MODAL) { it: ModalAlertModel -> modalAlertBuilder(requireContext(), it) }

        viewModel.on(DELETE_SUCCESS) {
            parentFragmentManager.setFragmentResult("deleteActiveShopList", bundleOf())
        }

        binding.delete.setOnClickListener { viewModel.requestDelete() }

        binding.panelBottomPanel.btn.setOnClickListener { viewModel.requestSave(domain) }

        parentFragmentManager
            .setFragmentResultListener("shopListManageChanged", viewLifecycleOwner) { _, it ->
                viewModel.changeShopList(it.getInt("id"))
            }
    }
}
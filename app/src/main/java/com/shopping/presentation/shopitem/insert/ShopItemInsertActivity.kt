package com.shopping.presentation.shopitem.insert

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.shopping.core.domain.ShopItem
import com.shopping.databinding.ActivityShopitemInsertBinding
import com.shopping.databinding.ItemShopitemInsertBinding
import com.shopping.presentation.main.MainActivity
import com.shopping.presentation.shopitem.insert.ShopItemInsertEvent.*
import com.shopping.utils.recyclerview.EasyAdapter
import com.shopping.utils.snackbar.SnackBarModel
import com.shopping.utils.snackbar.snackBarBuilder
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopItemInsertActivity : AppCompatActivity() {
    private var binding: ActivityShopitemInsertBinding? = null
    private val viewModel: ShopItemInsertActivityViewModel by viewModel()
    private val shoplistId by lazy { intent.getIntExtra("shoplistId", 1) }
    private val adapter = EasyAdapter(ItemShopitemInsertBinding::inflate) { it: ShopItem ->
        shopitem = it
        input.setText(it.quantity!!.toString())
        input.requestFocus()
        input.selectAll()
        add.setOnClickListener { _ -> viewModel.changeQuantity(it, it.quantity!! + 1) }
        remove.setOnClickListener { _ -> viewModel.changeQuantity(it, it.quantity!! - 1) }
        input.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && event == null) {
                binding?.input?.requestFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
    private val domain: ShopItem
        get() = ShopItem(viewModel.items.value.size, binding!!.input.text.toString(), 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.start(shoplistId)
        super.onCreate(savedInstanceState)
        binding = ActivityShopitemInsertBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        val binding = requireNotNull(binding)

        lifecycleScope.launchWhenResumed { viewModel.items.collect { adapter.submitList(it) } }

        viewModel.on(SHOW_ALERT) { it: SnackBarModel -> snackBarBuilder(binding.insertAll, it) }

        viewModel.on(CLEAR_INPUT) { binding.input.text.clear() }

        viewModel.on(BACK_TO_HOME) { backToHome() }

        binding.list.adapter = adapter
        binding.insertAll.setOnClickListener { viewModel.addAll() }
        binding.add.setOnClickListener {
            viewModel.addShopItem(domain)
        }

        binding.input.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && event == null) {
                viewModel.addShopItem(domain)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun backToHome() = startActivity(
        Intent(this, MainActivity::class.java).putExtra("shoplistId", shoplistId)
    )

    override fun onBackPressed() = backToHome()

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }
}
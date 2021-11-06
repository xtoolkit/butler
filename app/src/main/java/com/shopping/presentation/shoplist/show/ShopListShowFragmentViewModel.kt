package com.shopping.presentation.shoplist.show

import androidx.lifecycle.viewModelScope
import com.shopping.core.interactor.GetAllShopListUC
import com.shopping.framework.TopAppViewModel
import com.shopping.presentation.shoplist.show.ShopListShowEvents.REQUEST_CHANGE_SHOP_LIST
import com.shopping.presentation.shoplist.show.converter.toDomain
import com.shopping.presentation.shoplist.show.converter.toShopListShowUiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShopListShowFragmentViewModel(private val getAllShopListUC: GetAllShopListUC) :
    TopAppViewModel<ShopListShowEvents>() {
    private var shopListsLoaded = false
    val list = MutableStateFlow(listOf<ShopListShowUIItem>())
    private var select = 1

    fun selectShopList(shopListShowUIItem: ShopListShowUIItem, onlyUiUpdate: Boolean = false) =
        viewModelScope.launch {
            if (!onlyUiUpdate) trigger(REQUEST_CHANGE_SHOP_LIST, shopListShowUIItem.toDomain())
            select = shopListShowUIItem.id
            updateListUIState(list.value)
        }

    private suspend fun updateListUIState(input: List<ShopListShowUIItem>) {
        val output = input.map { it.copy() }
        output.forEachIndexed { i, item ->
            item.selected = item.id == select
            item.hideBorder = i == 0 || item.selected
            output.getOrNull(i - 1)?.takeIf { it.selected }?.let { item.hideBorder = true }
        }
        list.emit(output)
    }

    fun start() = viewModelScope.launch(Dispatchers.IO) {
        if (!shopListsLoaded) getAllShopListUC(Unit).onSuccess { target ->
            target.collect { new ->
                updateListUIState(new.map { it.toShopListShowUiItem() })
            }
            shopListsLoaded = true
        }
    }
}
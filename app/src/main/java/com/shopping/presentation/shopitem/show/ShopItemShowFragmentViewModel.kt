package com.shopping.presentation.shopitem.show

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.GetAllShopItemUC
import com.shopping.core.interactor.GetShopListUC
import com.shopping.core.interactor.UpdateShopItemUC
import com.shopping.framework.TopAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShopItemShowFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val getAllShopItemUC: GetAllShopItemUC,
    private val updateShopItemUC: UpdateShopItemUC
) :
    TopAppViewModel<ShopItemShowEvents>() {
    private var lastListJob: Job? = null
    val shopList = MutableStateFlow<ShopList?>(null)
    val items = MutableStateFlow(listOf<ShopItem>())

    fun changeShopList(newId: Int) {
        lastListJob?.cancel()
        lastListJob = viewModelScope.launch(Dispatchers.IO) {
            getShopListUC(ShopList(newId, "")).onSuccess {
                shopList.emit(it)
                getAllShopItemUC(it).onSuccess { list ->
                    list.collect { x -> items.emit(x) }
                }
            }
        }
    }

    fun toggleDone(shopItem: ShopItem) = viewModelScope.launch(Dispatchers.IO) {
        updateShopItemUC(
            shopList.value!! to ShopItem(
                shopItem.id,
                shopItem.name,
                shopItem.quantity,
                !shopItem.done
            )
        )
    }
}
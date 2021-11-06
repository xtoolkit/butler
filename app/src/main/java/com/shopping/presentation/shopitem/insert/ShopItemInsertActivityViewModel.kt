package com.shopping.presentation.shopitem.insert

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.AddShopItemUC
import com.shopping.core.interactor.GetShopItemUC
import com.shopping.core.interactor.GetShopListUC
import com.shopping.framework.TopAppViewModel
import com.shopping.presentation.shopitem.insert.ShopItemInsertEvent.*
import com.shopping.utils.snackbar.SnackBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShopItemInsertActivityViewModel(
    private val getShopListUC: GetShopListUC,
    private val getShopItemUC: GetShopItemUC,
    private val addShopItemUC: AddShopItemUC
) : TopAppViewModel<ShopItemInsertEvent>() {
    var shoplist = ShopList(-1, "")
    val items = MutableStateFlow(listOf<ShopItem>())

    private fun checkNewShopItemValidity(shopItem: ShopItem): Result<Boolean> {
        if (shopItem.name.isBlank()) return Result.failure(Exception("Shopitem name cannot be empty."))
        items.value.find { it.name == shopItem.name }
            ?.let { return Result.failure(Exception("Item already exists.")) }
        return Result.success(true)
    }

    fun addShopItem(shopItem: ShopItem) = viewModelScope.launch(Dispatchers.IO) {
        var task: Result<Any> = checkNewShopItemValidity(shopItem)
            .onFailure { trigger(SHOW_ALERT, SnackBarModel(it.message!!)) }

        if (task.isSuccess) task = getShopItemUC(shoplist to shopItem)
            .onSuccess { trigger(SHOW_ALERT, SnackBarModel("Item already exists.")) }
        else return@launch

        if (task.isFailure) {
            items.emit(items.value.map { it.copy() }.toMutableList().apply { add(shopItem) })
            trigger(CLEAR_INPUT)
        }
    }

    fun changeQuantity(shopItem: ShopItem, newQuantity: Int) {
        items.value = items.value
            .map {
                if (it.id == shopItem.id) ShopItem(
                    it.id,
                    it.name,
                    newQuantity
                ) else it.copy()
            }
            .filter { it.quantity!! >= 0 }
    }

    fun addAll() = viewModelScope.launch(Dispatchers.IO) {
        items.value.forEach {
            addShopItemUC(
                shoplist to ShopItem(0, it.name, if (it.quantity == null) 0 else it.quantity)
            )
        }
        trigger(BACK_TO_HOME)
    }

    fun start(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (shoplist.id == -1) getShopListUC(ShopList(id, ""))
            .onSuccess { shoplist = it }
    }
}
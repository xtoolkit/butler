package com.shopping.presentation.shopitem.show

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.AddShopItemUC
import com.shopping.core.interactor.GetAllShopItemUC
import com.shopping.core.interactor.GetShopListUC
import com.shopping.core.interactor.UpdateShopItemUC
import com.shopping.framework.TopAppViewModel
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.EDIT_MODE_CHANGED
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.SHOW_ALERT
import com.shopping.utils.snackbar.SnackBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShopItemShowFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val getAllShopItemUC: GetAllShopItemUC,
    private val addShopItemUC: AddShopItemUC,
    private val updateShopItemUC: UpdateShopItemUC
) : TopAppViewModel<ShopItemShowEvents>() {
    private var lastListJob: Job? = null
    private var lastItemsJob: Job? = null
    private var lastListFlow: Flow<List<ShopItem>>? = null
    private var isDone: Boolean? = null
    var isEdit = false
    val shopList = MutableStateFlow<ShopList?>(null)
    val items = MutableStateFlow(listOf<ShopItem>())

    fun changeShopList(newId: Int) {
        isEdit = false
        trigger(EDIT_MODE_CHANGED)
        lastListJob?.cancel()
        lastListJob = viewModelScope.launch(Dispatchers.IO) {
            getShopListUC(ShopList(newId, "")).onSuccess {
                shopList.emit(it)
                changeShopItemsShow(null)
            }
        }
    }

    fun changeShopItemsShow(isDone: Boolean?) {
        this.isDone = isDone
        getShopItems(isDone)
    }

    private fun getShopItems(isDone: Boolean?) = shopList.value?.let {
        lastItemsJob?.cancel()
        lastItemsJob = viewModelScope.launch(Dispatchers.IO) {
            getAllShopItemUC(it to isDone).onSuccess { list ->
                lastListFlow = list
                list.collect { x -> if (!isEdit) items.emit(x) }
            }
        }
    }

    fun toggleEdit() = viewModelScope.launch(Dispatchers.IO) {
        isEdit = !isEdit
        if (isEdit) items.emit(items.value.map {
            ShopItem(it.id, it.name, if (it.quantity == null) 1 else it.quantity, it.done)
        }) else {
            var updated = false
            val originList = lastListFlow!!.first()
            val currentList = items.value.map { it.copy() }

            // find new items
            val newItems = currentList.filter { it.name !in originList.map { item -> item.name } }

            // insert new items
            newItems.forEach { item ->
                updated = true
                addShopItemUC(
                    shopList.value!! to ShopItem(
                        0,
                        item.name,
                        if (item.quantity == 1) null else item.quantity
                    )
                )
            }

            // update old items if needs
            currentList
                .filter { it.name !in newItems.map { item -> item.name } }
                .filter { it != originList.find { item -> item.id == it.id } }
                .forEach {
                    updateShopItemUC(
                        shopList.value!! to ShopItem(
                            it.id,
                            it.name,
                            if (it.quantity == 1) null else it.quantity,
                            it.done
                        )
                    )
                    updated = true
                }

            if (!updated) items.emit(originList)
        }
        trigger(EDIT_MODE_CHANGED)
    }

    private fun checkNewShopItemValidity(shopItem: ShopItem): Result<Boolean> {
        if (shopItem.name.isBlank()) return Result.failure(Exception("Shopitem name cannot be empty."))
        items.value.find { it.name == shopItem.name }
            ?.let { return Result.failure(Exception("Item already exists.")) }
        return Result.success(true)
    }

    fun addShopItem(shopItem: ShopItem) = checkNewShopItemValidity(shopItem)
        .onFailure { trigger(SHOW_ALERT, SnackBarModel(it.message!!)) }
        .onSuccess {
            items.value = items.value.toMutableList().apply { add(shopItem) }
        }

    fun changeQuantity(shopItem: ShopItem, newQuantity: Int) {
        if (newQuantity < 1) trigger(SHOW_ALERT, SnackBarModel("delete?"))
        else items.value = items.value.map {
            if (shopItem.id == it.id) ShopItem(it.id, it.name, newQuantity, it.done)
            else it
        }
    }

    fun toggleDone(shopItem: ShopItem) = viewModelScope.launch(Dispatchers.IO) {
        val item = ShopItem(shopItem.id, shopItem.name, shopItem.quantity, !shopItem.done)
        updateShopItemUC(shopList.value!! to item)
        if (isEdit) items.value = items.value.map { if (item.id == it.id) item else it }
    }
}
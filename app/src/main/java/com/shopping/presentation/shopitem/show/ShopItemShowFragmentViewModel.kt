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
import com.shopping.presentation.shopitem.show.converter.toDomain
import com.shopping.presentation.shopitem.show.converter.toShopItemShowUIItem
import com.shopping.utils.snackbar.SnackBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShopItemShowFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val getAllShopItemUC: GetAllShopItemUC,
    private val addShopItemUC: AddShopItemUC,
    private val updateShopItemUC: UpdateShopItemUC
) : TopAppViewModel<ShopItemShowEvents>() {
    private var lastListJob: Job? = null
    private var isDone: Boolean? = false
    var isEdit = false
    val shopList = MutableStateFlow<ShopList?>(null)
    val items = MutableStateFlow(listOf<ShopItemShowUIItem>())

    fun changeShopList(newId: Int) {
        isEdit = false
        trigger(EDIT_MODE_CHANGED)
        lastListJob?.cancel()
        lastListJob = viewModelScope.launch(Dispatchers.IO) {
            getShopListUC(ShopList(newId, "")).onSuccess {
                shopList.emit(it)
                changeShopItemsShow(false)
            }
        }
    }

    fun changeShopItemsShow(isDone: Boolean?) {
        this.isDone = isDone
        setShopItems(isDone)
    }

    private fun setShopItems(isDone: Boolean?) = viewModelScope.launch(Dispatchers.IO) {
        shopList.value?.let {
            getAllShopItemUC(it to isDone).onSuccess { list ->
                items.emit(list.first().map { item -> item.toShopItemShowUIItem() })
            }
        }
    }

    fun toggleEdit() = viewModelScope.launch(Dispatchers.IO) {
        isEdit = !isEdit
        if (isEdit) changeShopItemsShow(null)
        else {
            items.value.filter { it.isNew }.forEach {
                addShopItemUC(shopList.value!! to it.toDomain().copy(id = 0))
            }
            items.value.filter { it.updated }.forEach {
                updateShopItemUC(shopList.value!! to it.toDomain())
            }
            changeShopItemsShow(false)
        }
        trigger(EDIT_MODE_CHANGED)
    }

    private fun checkNewShopItemValidity(shopItem: ShopItem): Result<Boolean> {
        if (shopItem.name.isBlank())
            return Result.failure(Exception("Shopitem name cannot be empty."))

        items.value
            .find { it.name == shopItem.name }
            ?.let { return Result.failure(Exception("Item already exists.")) }

        return Result.success(true)
    }

    fun addShopItem(item: ShopItemShowUIItem) = checkNewShopItemValidity(item.toDomain())
        .onFailure { trigger(SHOW_ALERT, SnackBarModel(it.message!!)) }
        .onSuccess { items.value = items.value.toMutableList().apply { add(item) } }

    fun changeQuantity(item: ShopItemShowUIItem, newQuantity: Int) {
        if (newQuantity < 1) trigger(SHOW_ALERT, SnackBarModel("delete?"))
        else items.value = items.value.map {
            if (item.id == it.id) it.copy(quantity = newQuantity, updated = !item.isNew)
            else it
        }
    }

    fun toggleDone(item: ShopItemShowUIItem) = viewModelScope.launch(Dispatchers.IO) {
        if (item.done && isEdit && !item.updated) return@launch
        val newItem = item.copy(done = !item.done)
        updateShopItemUC(shopList.value!! to newItem.toDomain())
        items.emit(items.value.map { if (item.id == it.id) newItem else it })
    }
}
package com.shopping.presentation.shopitem.show

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.*
import com.shopping.framework.TopAppViewModel
import com.shopping.presentation.shopitem.show.ShopItemShowEvents.*
import com.shopping.presentation.shopitem.show.converter.toDomain
import com.shopping.presentation.shopitem.show.converter.toShopItemShowUIItem
import com.shopping.utils.modalalert.ModalAlertModel
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
    private val updateShopItemUC: UpdateShopItemUC,
    private val deleteShopItemUC: DeleteShopItemUC
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

    fun requestAddShopItem(item: ShopItemShowUIItem) = checkNewShopItemValidity(item.toDomain())
        .onFailure { trigger(SHOW_ALERT, SnackBarModel(it.message!!)) }
        .onSuccess { items.value = items.value.toMutableList().apply { add(item) } }

    fun requestChangeQuantity(item: ShopItemShowUIItem, newQuantity: Int) =
        items.value.find { it.id == item.id }?.let {
            it.quantity = newQuantity
            it.updated = true
        }

    fun requestDeleteShopItem(item: ShopItemShowUIItem) = trigger(SHOW_MODAL,
        ModalAlertModel("Warning", "Are you sure to delete `${item.name}`?") {
            viewModelScope.launch(Dispatchers.IO) {
                deleteShopItemUC(shopList.value!! to item.toDomain()).onSuccess {
                    items.emit(items.value.filter { it.id != item.id })
                }
            }
        }
    )

    fun requestToggleDone(item: ShopItemShowUIItem) = viewModelScope.launch(Dispatchers.IO) {
        /*
            disable toggleDone when item default done in edit mode
            if (isEdit && item.done && !item.updated) return@launch
        */
        val newItem = item.copy(done = !item.done, updated = true)
        if(!isEdit) updateShopItemUC(shopList.value!! to newItem.toDomain())
        items.emit(items.value.map { if (item.id == it.id) newItem else it })
    }
}
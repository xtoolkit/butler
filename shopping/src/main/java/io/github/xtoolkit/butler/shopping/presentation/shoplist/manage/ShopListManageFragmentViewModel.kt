package io.github.xtoolkit.butler.shopping.presentation.shoplist.manage

import androidx.lifecycle.viewModelScope
import io.github.xtoolkit.butler.utils.BaseViewModel
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.core.interactor.GetShopListUC
import io.github.xtoolkit.butler.shopping.core.interactor.UpdateShopListUC
import io.github.xtoolkit.butler.shopping.presentation.shoplist.manage.ShopListManageEvents.*
import io.github.xtoolkit.butler.utils.snackbar.SnackBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShopListManageFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val updateShopListUC: UpdateShopListUC
) : BaseViewModel<ShopListManageEvents>() {
    val shopList = MutableStateFlow<ShopList?>(null)

    fun changeShopList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        trigger(NAME_ERROR, null)
        getShopListUC(ShopList(id, "")).onSuccess { shopList.emit(it) }
    }

    private suspend fun checkShopListValidity(item: ShopList): Result<Boolean> {
        if (item.name.isBlank()) return Result.failure(Exception("Shoplist name cannot be empty."))
        if (shopList.value!!.name != item.name) getShopListUC(item.copy(0)).onSuccess {
            return Result.failure(Exception("List name already exists."))
        }
        return Result.success(true)
    }

    fun requestSave(item: ShopList) = viewModelScope.launch(Dispatchers.IO) {
        checkShopListValidity(item).onFailure { trigger(NAME_ERROR, it.message); return@launch }
        if (item != shopList.value) updateShopListUC(item)
        trigger(UPDATE_SUCCESS)
        trigger(NAME_ERROR, null)
        trigger(SHOW_ALERT, SnackBarModel("Your Shop list updated"))
    }
}
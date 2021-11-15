package com.shopping.presentation.shoplist.manage

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.GetShopListUC
import com.shopping.core.interactor.UpdateShopListUC
import com.shopping.framework.TopAppViewModel
import com.shopping.presentation.shoplist.manage.ShopListManageEvents.*
import com.shopping.utils.snackbar.SnackBarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShopListManageFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val updateShopListUC: UpdateShopListUC
) : TopAppViewModel<ShopListManageEvents>() {
    val shopList = MutableStateFlow<ShopList?>(null)

    fun changeShopList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        trigger(NAME_ERROR, null)
        getShopListUC(ShopList(id, "")).onSuccess { shopList.emit(it) }
    }

    private fun checkShopListValidity(shopList: ShopList): Result<Boolean> {
        if (shopList.name.isBlank()) return Result.failure(Exception("Shop list name cannot be empty."))
        return Result.success(true)
    }

    fun requestSave(shopList: ShopList) = viewModelScope.launch(Dispatchers.IO) {
        checkShopListValidity(shopList).onFailure { trigger(NAME_ERROR, it.message); return@launch }
        updateShopListUC(shopList).onSuccess {
            trigger(UPDATE_SUCCESS)
            trigger(NAME_ERROR, null)
            trigger(SHOW_ALERT, SnackBarModel("Your Shop list updated"))
        }
    }
}
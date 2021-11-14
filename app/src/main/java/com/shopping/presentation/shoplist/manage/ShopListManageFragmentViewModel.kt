package com.shopping.presentation.shoplist.manage

import androidx.lifecycle.viewModelScope
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.GetShopListUC
import com.shopping.core.interactor.UpdateShopListUC
import com.shopping.framework.TopAppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShopListManageFragmentViewModel(
    private val getShopListUC: GetShopListUC,
    private val updateShopListUC: UpdateShopListUC
) : TopAppViewModel<ShopListManageEvents>() {
    val shopList = MutableStateFlow<ShopList?>(null)

    fun changeShopList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        getShopListUC(ShopList(id, "")).onSuccess { shopList.emit(it) }
    }
}
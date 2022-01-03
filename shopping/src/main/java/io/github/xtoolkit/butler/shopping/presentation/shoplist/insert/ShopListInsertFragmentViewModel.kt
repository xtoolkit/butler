package io.github.xtoolkit.butler.shopping.presentation.shoplist.insert

import androidx.lifecycle.viewModelScope
import io.github.xtoolkit.butler.utils.BaseViewModel
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.core.interactor.AddShopListUC
import io.github.xtoolkit.butler.shopping.core.interactor.GetShopListUC
import io.github.xtoolkit.butler.shopping.presentation.shoplist.insert.ShopListInsertEvents.DISMISS_FRAGMENT
import io.github.xtoolkit.butler.shopping.presentation.shoplist.insert.ShopListInsertEvents.NAME_VALIDITY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopListInsertFragmentViewModel(
    private val addShopListUC: AddShopListUC,
    private val getShopListUC: GetShopListUC
) : BaseViewModel<ShopListInsertEvents>() {
    private suspend fun checkNewShopListValidity(shopList: ShopList): Result<Boolean> {
        if (shopList.name.isBlank()) return Result.failure(Exception("Shoplist name cannot be empty."))
        getShopListUC(shopList).onSuccess {
            return Result.failure(Exception("List name already exists."))
        }
        return Result.success(true)
    }

    fun addShopList(shopList: ShopList) = viewModelScope.launch(Dispatchers.IO) {
        val task = checkNewShopListValidity(shopList)
            .onFailure { trigger(NAME_VALIDITY, it.message) }
            .onSuccess { trigger(NAME_VALIDITY, null) }

        if (task.isSuccess) addShopListUC(shopList).onSuccess { trigger(DISMISS_FRAGMENT) }
    }
}
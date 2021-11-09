package com.shopping.core.interactor

import com.shopping.core.data.repositories.ShopItemRepo
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class GetAllShopItemUC(shopItemRepo: ShopItemRepo) :
    BaseUseCase<Pair<ShopList, Boolean?>, Flow<List<ShopItem>>, ShopItemRepo>(shopItemRepo) {
    override suspend fun execute(parameter: Pair<ShopList, Boolean?>) =
        repo.getAllShopItem(parameter.first, parameter.second)
}
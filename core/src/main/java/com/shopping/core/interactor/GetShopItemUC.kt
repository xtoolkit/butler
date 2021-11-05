package com.shopping.core.interactor

import com.shopping.core.data.repositories.ShopItemRepo
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.base.BaseUseCase

class GetShopItemUC(shopItemRepo: ShopItemRepo) :
    BaseUseCase<Pair<ShopList, ShopItem>, ShopItem, ShopItemRepo>(shopItemRepo) {
    override suspend fun execute(parameter: Pair<ShopList, ShopItem>) =
        repo.getShopItem(parameter.first, parameter.second)
}
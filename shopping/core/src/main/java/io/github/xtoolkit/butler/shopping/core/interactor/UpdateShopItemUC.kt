package io.github.xtoolkit.butler.shopping.core.interactor

import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopItemRepo
import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.utils.BaseUseCase

class UpdateShopItemUC(shopItemRepo: ShopItemRepo) :
    BaseUseCase<Pair<ShopList, ShopItem>, ShopItem, ShopItemRepo>(shopItemRepo) {
    override suspend fun execute(parameter: Pair<ShopList, ShopItem>) =
        repo.updateShopItem(parameter.first, parameter.second)
}
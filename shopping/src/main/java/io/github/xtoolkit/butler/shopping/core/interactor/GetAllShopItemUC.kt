package io.github.xtoolkit.butler.shopping.core.interactor

import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopItemRepo
import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.utils.BaseUseCase
import kotlinx.coroutines.flow.Flow

class GetAllShopItemUC(shopItemRepo: ShopItemRepo) :
    BaseUseCase<Pair<ShopList, Boolean?>, Flow<List<ShopItem>>, ShopItemRepo>(shopItemRepo) {
    override suspend fun execute(parameter: Pair<ShopList, Boolean?>) =
        repo.getAllShopItem(parameter.first, parameter.second)
}
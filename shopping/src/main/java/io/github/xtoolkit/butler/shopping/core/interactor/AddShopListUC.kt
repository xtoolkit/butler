package io.github.xtoolkit.butler.shopping.core.interactor

import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopListRepo
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.core.interactor.base.BaseUseCase

class AddShopListUC(shopListRepo: ShopListRepo) :
    BaseUseCase<ShopList, ShopList, ShopListRepo>(shopListRepo) {
    override suspend fun execute(parameter: ShopList) = repo.addShopList(parameter)
}
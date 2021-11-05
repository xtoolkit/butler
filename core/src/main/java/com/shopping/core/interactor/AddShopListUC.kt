package com.shopping.core.interactor

import com.shopping.core.data.repositories.ShopListRepo
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.base.BaseUseCase

class AddShopListUC(shopListRepo: ShopListRepo) :
    BaseUseCase<ShopList, ShopList, ShopListRepo>(shopListRepo) {
    override suspend fun execute(parameter: ShopList) = repo.addShopList(parameter)
}
package com.shopping.core.interactor

import com.shopping.core.data.repositories.ShopListRepo
import com.shopping.core.domain.ShopList
import com.shopping.core.interactor.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class GetAllShopListUC(shopListRepo: ShopListRepo) :
    BaseUseCase<Unit, Flow<List<ShopList>>, ShopListRepo>(shopListRepo) {
    override suspend fun execute(parameter: Unit) = repo.getAllShopList()
}
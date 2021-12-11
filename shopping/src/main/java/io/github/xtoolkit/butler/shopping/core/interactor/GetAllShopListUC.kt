package io.github.xtoolkit.butler.shopping.core.interactor

import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopListRepo
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.core.interactor.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class GetAllShopListUC(shopListRepo: ShopListRepo) :
    BaseUseCase<Unit, Flow<List<ShopList>>, ShopListRepo>(shopListRepo) {
    override suspend fun execute(parameter: Unit) = repo.getAllShopList()
}
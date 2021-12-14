package io.github.xtoolkit.butler.shopping.core.data.datasource

import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import kotlinx.coroutines.flow.Flow

interface ShopListDS {
    suspend fun add(shopList: ShopList): Result<ShopList>
    suspend fun get(shopList: ShopList): Result<ShopList>
    suspend fun getAll(): Result<Flow<List<ShopList>>>
    suspend fun update(shopList: ShopList): Result<ShopList>
}
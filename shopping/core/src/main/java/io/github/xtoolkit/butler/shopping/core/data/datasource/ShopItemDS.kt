package io.github.xtoolkit.butler.shopping.core.data.datasource

import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import kotlinx.coroutines.flow.Flow

interface ShopItemDS {
    suspend fun add(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun get(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun getAll(shopList: ShopList, isDone: Boolean?): Result<Flow<List<ShopItem>>>
    suspend fun update(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun delete(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
}
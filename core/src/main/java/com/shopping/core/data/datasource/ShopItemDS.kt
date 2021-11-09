package com.shopping.core.data.datasource

import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import kotlinx.coroutines.flow.Flow

interface ShopItemDS {
    suspend fun add(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun get(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun getAll(shopList: ShopList): Result<Flow<List<ShopItem>>>
    suspend fun update(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
    suspend fun delete(shopList: ShopList, shopItem: ShopItem): Result<ShopItem>
}
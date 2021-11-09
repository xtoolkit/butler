package com.shopping.core.data.repositories

import com.shopping.core.data.datasource.ShopItemDS
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList

class ShopItemRepo(private val shopItemDS: ShopItemDS) {
    suspend fun addShopItem(shopList: ShopList, shopItem: ShopItem) =
        shopItemDS.add(shopList, shopItem)

    suspend fun getShopItem(shopList: ShopList, shopItem: ShopItem) =
        shopItemDS.get(shopList, shopItem)

    suspend fun getAllShopItem(shopList: ShopList, isDone: Boolean? = null) =
        shopItemDS.getAll(shopList, isDone)

    suspend fun updateShopItem(shopList: ShopList, shopItem: ShopItem) =
        shopItemDS.update(shopList, shopItem)

    suspend fun deleteShopItem(shopList: ShopList, shopItem: ShopItem) =
        shopItemDS.delete(shopList, shopItem)
}
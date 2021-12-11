package io.github.xtoolkit.butler.shopping.core.data.repositories

import io.github.xtoolkit.butler.shopping.core.data.datasource.ShopItemDS
import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.core.domain.ShopList

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
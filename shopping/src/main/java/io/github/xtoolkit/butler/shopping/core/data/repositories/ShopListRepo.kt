package io.github.xtoolkit.butler.shopping.core.data.repositories

import io.github.xtoolkit.butler.shopping.core.data.datasource.ShopListDS
import io.github.xtoolkit.butler.shopping.core.domain.ShopList

class ShopListRepo(private val shopListDS: ShopListDS) {
    suspend fun addShopList(shopList: ShopList) = shopListDS.add(shopList)
    suspend fun getShopList(shopList: ShopList) = shopListDS.get(shopList)
    suspend fun getAllShopList() = shopListDS.getAll()
    suspend fun updateShopList(shopList: ShopList) = shopListDS.update(shopList)
}
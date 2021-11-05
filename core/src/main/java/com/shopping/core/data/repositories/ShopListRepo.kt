package com.shopping.core.data.repositories

import com.shopping.core.data.datasource.ShopListDS
import com.shopping.core.domain.ShopList

class ShopListRepo(private val shopListDS: ShopListDS) {
    suspend fun addShopList(shopList: ShopList) = shopListDS.add(shopList)
    suspend fun getShopList(shopList: ShopList) = shopListDS.get(shopList)
    suspend fun getAllShopList() = shopListDS.getAll()
    suspend fun updateShopList(shopList: ShopList) = shopListDS.update(shopList)
}
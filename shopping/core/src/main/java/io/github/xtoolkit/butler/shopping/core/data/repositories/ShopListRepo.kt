package io.github.xtoolkit.butler.shopping.core.data.repositories

import io.github.xtoolkit.butler.shopping.core.data.datasource.ShopItemDS
import io.github.xtoolkit.butler.shopping.core.data.datasource.ShopListDS
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import kotlinx.coroutines.flow.first

class ShopListRepo(private val shopListDS: ShopListDS, private val shopItemDS: ShopItemDS) {
    suspend fun addShopList(shopList: ShopList) = shopListDS.add(shopList)
    suspend fun getShopList(shopList: ShopList) = shopListDS.get(shopList)
    suspend fun getAllShopList() = shopListDS.getAll()
    suspend fun updateShopList(shopList: ShopList) = shopListDS.update(shopList)
    suspend fun deleteShopList(shopList: ShopList) = try {
        shopItemDS.getAll(shopList, null).getOrThrow().first().forEach {
            shopItemDS.delete(shopList, it)
        }
        shopListDS.delete(shopList)
        Result.success(shopList)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
package com.shopping.framework.db.ds

import com.shopping.core.data.datasource.ShopItemDS
import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.framework.db.dao.ShopItemDao
import com.shopping.framework.db.entity.contverter.toDomain
import com.shopping.framework.db.entity.contverter.toEntity
import kotlinx.coroutines.flow.map

class LocalShopItemDS(private val shopItemDao: ShopItemDao) : ShopItemDS {
    override suspend fun add(shopList: ShopList, shopItem: ShopItem) = Result.success(
        shopItemDao.getById(shopItemDao.add(shopItem.toEntity(shopList)).toInt())!!.toDomain()
    )

    override suspend fun get(shopList: ShopList, shopItem: ShopItem): Result<ShopItem> {
        shopItemDao.getById(shopItem.id)?.let {
            return Result.success(it.toDomain())
        }
        shopItemDao.getByNameInShopList(shopList.id, shopItem.name)?.let {
            return Result.success(it.toDomain())
        }
        return Result.failure(Exception("ShopItem cannot be found."))
    }

    override suspend fun getAll(shopList: ShopList) = Result.success(
        shopItemDao.getAll(shopList.id).map { list -> list.map { it.toDomain() } }
    )

    override suspend fun update(shopList: ShopList, shopItem: ShopItem) = shopItemDao
        .update(shopItem.toEntity(shopList))
        .run { return@run Result.success(shopItem) }

    override suspend fun delete(shopList: ShopList, shopItem: ShopItem) = shopItemDao
        .delete(shopItem.toEntity(shopList))
        .run { return@run Result.success(shopItem) }
}
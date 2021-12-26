package io.github.xtoolkit.butler.shopping.framework.db.ds

import io.github.xtoolkit.butler.shopping.core.data.datasource.ShopListDS
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.framework.db.dao.ShopListDao
import io.github.xtoolkit.butler.shopping.framework.db.entity.contverter.toDomain
import io.github.xtoolkit.butler.shopping.framework.db.entity.contverter.toEntity
import kotlinx.coroutines.flow.map

class LocalShopListDS(private val shopListDao: ShopListDao) : ShopListDS {
    override suspend fun add(shopList: ShopList) = Result.success(
        shopListDao.getById(shopListDao.add(shopList.toEntity()).toInt())!!.toDomain()
    )

    override suspend fun get(shopList: ShopList): Result<ShopList> {
        shopListDao.getById(shopList.id)?.let {
            return Result.success(it.toDomain())
        }
        shopListDao.getByName(shopList.name)?.let {
            return Result.success(it.toDomain())
        }
        return Result.failure(Exception("ShopList cannot be found."))
    }

    override suspend fun getAll() =
        Result.success(shopListDao.getAll().map { it.map { x -> x.toDomain() } })

    override suspend fun update(shopList: ShopList) = shopListDao
        .update(shopList.toEntity())
        .run { return@run Result.success(shopList) }

    override suspend fun delete(shopList: ShopList) = shopListDao
        .delete(shopList.toEntity())
        .run { return@run Result.success(shopList) }
}
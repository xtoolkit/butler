package com.shopping.framework.db.dao

import androidx.room.*
import com.shopping.framework.db.entity.ShopItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(shopItemEntity: ShopItemEntity): Long

    @Query("select * from shop_items where listId = :shopListId")
    fun getAll(shopListId: Int): Flow<List<ShopItemEntity>>

    @Query("select * from shop_items where id = :id")
    suspend fun getById(id: Int): ShopItemEntity?

    @Query("select * from shop_items where listId = :shopListId and name = :name")
    suspend fun getByNameInShopList(shopListId: Int, name: String): ShopItemEntity?

    @Update
    suspend fun update(shopItemEntity: ShopItemEntity)
}
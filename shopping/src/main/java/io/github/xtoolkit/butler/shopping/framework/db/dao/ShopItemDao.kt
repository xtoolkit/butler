package io.github.xtoolkit.butler.shopping.framework.db.dao

import androidx.room.*
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(shopItemEntity: ShopItemEntity): Long

    @Query("select * from shop_items where listId = :shopListId")
    fun getAll(shopListId: Int): Flow<List<ShopItemEntity>>

    @Query("select * from shop_items where listId = :shopListId AND done = :isDone")
    fun getAllByDone(shopListId: Int, isDone: Boolean): Flow<List<ShopItemEntity>>

    @Query("select * from shop_items where id = :id")
    suspend fun getById(id: Int): ShopItemEntity?

    @Query("select * from shop_items where listId = :shopListId and name = :name")
    suspend fun getByNameInShopList(shopListId: Int, name: String): ShopItemEntity?

    @Update
    suspend fun update(shopItemEntity: ShopItemEntity)

    @Delete
    suspend fun delete(shopItemEntity: ShopItemEntity)
}
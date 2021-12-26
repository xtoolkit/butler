package io.github.xtoolkit.butler.shopping.framework.db.dao

import androidx.room.*
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(shopListEntity: ShopListEntity): Long

    @Query("select * from shop_lists")
    fun getAll(): Flow<List<ShopListEntity>>

    @Query("select * from shop_lists where id = :id")
    suspend fun getById(id: Int): ShopListEntity?

    @Query("select * from shop_lists where name = :name")
    suspend fun getByName(name: String): ShopListEntity?

    @Update
    suspend fun update(shopListEntity: ShopListEntity)

    @Delete
    suspend fun delete(shopListEntity: ShopListEntity)
}
package com.shopping.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shopping.framework.db.dao.ShopItemDao
import com.shopping.framework.db.dao.ShopListDao
import com.shopping.framework.db.entity.ShopItemEntity
import com.shopping.framework.db.entity.ShopListEntity

@Database(
    entities = [ShopListEntity::class, ShopItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TopAppDatabase : RoomDatabase() {
    abstract fun shopItemDao(): ShopItemDao
    abstract fun shopListDao(): ShopListDao
}
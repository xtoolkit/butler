package io.github.xtoolkit.butler.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.xtoolkit.butler.shopping.framework.db.dao.ShopItemDao
import io.github.xtoolkit.butler.shopping.framework.db.dao.ShopListDao
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopItemEntity
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopListEntity

@Database(
    entities = [ShopListEntity::class, ShopItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ButlerDatabase : RoomDatabase() {
    abstract fun shopItemDao(): ShopItemDao
    abstract fun shopListDao(): ShopListDao
}
package com.shopping.framework.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val listId: Int,
    val name: String,
    val quantity: Int? = null,
    val done: Boolean = false
)
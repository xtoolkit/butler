package com.shopping.framework.db.entity.contverter

import com.shopping.core.domain.ShopItem
import com.shopping.core.domain.ShopList
import com.shopping.framework.db.entity.ShopItemEntity

fun ShopItem.toEntity(shopList: ShopList): ShopItemEntity = ShopItemEntity(
    this.id,
    shopList.id,
    this.name,
    this.quantity,
    this.done
)

fun ShopItemEntity.toDomain(): ShopItem = ShopItem(
    this.id,
    this.name,
    this.quantity,
    this.done
)
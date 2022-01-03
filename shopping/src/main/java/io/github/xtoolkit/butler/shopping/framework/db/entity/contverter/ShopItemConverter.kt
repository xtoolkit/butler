package io.github.xtoolkit.butler.shopping.framework.db.entity.contverter

import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopItemEntity

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
package com.shopping.presentation.shopitem.show.converter

import com.shopping.core.domain.ShopItem
import com.shopping.presentation.shopitem.show.ShopItemShowUIItem

fun ShopItem.toShopItemShowUIItem() = ShopItemShowUIItem(
    this.id,
    this.name,
    (if (this.quantity == null) 1 else this.quantity)!!,
    this.done
)

fun ShopItemShowUIItem.toDomain() = ShopItem(
    this.id,
    this.name,
    if (this.quantity > 1) this.quantity else null,
    this.done
)
package io.github.xtoolkit.butler.shopping.presentation.shopitem.show.converter

import io.github.xtoolkit.butler.shopping.core.domain.ShopItem
import io.github.xtoolkit.butler.shopping.presentation.shopitem.show.ShopItemShowUIItem

fun ShopItem.toShopItemShowUIItem() = ShopItemShowUIItem(
    this.id,
    this.name,
    this.quantity ?: 1,
    this.done
)

fun ShopItemShowUIItem.toDomain() = ShopItem(
    this.id,
    this.name,
    if (this.quantity > 1) this.quantity else null,
    this.done
)
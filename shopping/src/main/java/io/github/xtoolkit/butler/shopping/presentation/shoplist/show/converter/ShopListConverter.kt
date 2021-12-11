package io.github.xtoolkit.butler.shopping.presentation.shoplist.show.converter

import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.presentation.shoplist.show.ShopListShowUIItem

fun ShopList.toShopListShowUiItem() = ShopListShowUIItem(
    this.id,
    this.name,
    this.description
)

fun ShopListShowUIItem.toDomain() = ShopList(
    this.id,
    this.name,
    this.description
)
package com.shopping.presentation.shoplist.show.converter

import com.shopping.core.domain.ShopList
import com.shopping.presentation.shoplist.show.ShopListShowUIItem

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
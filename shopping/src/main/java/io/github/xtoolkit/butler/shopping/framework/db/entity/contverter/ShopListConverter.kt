package io.github.xtoolkit.butler.shopping.framework.db.entity.contverter

import io.github.xtoolkit.butler.shopping.core.domain.ShopList
import io.github.xtoolkit.butler.shopping.framework.db.entity.ShopListEntity

fun ShopList.toEntity(): ShopListEntity = ShopListEntity(
    this.id,
    this.name,
    this.description
)

fun ShopListEntity.toDomain(): ShopList = ShopList(
    this.id,
    this.name,
    this.description
)
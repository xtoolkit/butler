package com.shopping.framework.db.entity.contverter

import com.shopping.core.domain.ShopList
import com.shopping.framework.db.entity.ShopListEntity

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
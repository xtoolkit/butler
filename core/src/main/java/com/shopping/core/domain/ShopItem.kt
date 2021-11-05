package com.shopping.core.domain

data class ShopItem(
    val id: Int,
    val name: String,
    val quantity: Int? = null,
    val done: Boolean = false
)

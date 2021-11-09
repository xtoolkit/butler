package com.shopping.presentation.shopitem.show

data class ShopItemShowUIItem(
    val id: Int,
    val name: String,
    val quantity: Int = 1,
    val done: Boolean = false,
    val isNew: Boolean = false,
    val updated: Boolean = false
)
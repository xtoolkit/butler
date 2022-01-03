package io.github.xtoolkit.butler.shopping.presentation.shopitem.show

data class ShopItemShowUIItem(
    val id: Int,
    val name: String,
    var quantity: Int = 1,
    val done: Boolean = false,
    val isNew: Boolean = false,
    var updated: Boolean = false
)
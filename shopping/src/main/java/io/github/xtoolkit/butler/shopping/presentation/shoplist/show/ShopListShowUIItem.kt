package io.github.xtoolkit.butler.shopping.presentation.shoplist.show

data class ShopListShowUIItem(
    val id: Int,
    val name: String,
    val description: String? = null,
    var selected: Boolean = false,
    var hideBorder: Boolean = false,
)

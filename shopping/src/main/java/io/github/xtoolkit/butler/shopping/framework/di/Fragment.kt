package io.github.xtoolkit.butler.shopping.framework.di

import io.github.xtoolkit.butler.shopping.presentation.shoplist.insert.ShopListInsertFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val fragments = module {
    fragment { ShopListInsertFragment() }
}
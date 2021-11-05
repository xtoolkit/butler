package com.shopping.framework.di

import com.shopping.presentation.shoplist.insert.ShopListInsertFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val fragment = module {
    fragment { ShopListInsertFragment() }
}
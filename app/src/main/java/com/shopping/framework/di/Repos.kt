package com.shopping.framework.di

import com.shopping.core.data.repositories.ShopItemRepo
import com.shopping.core.data.repositories.ShopListRepo
import com.shopping.framework.db.ds.LocalShopItemDS
import com.shopping.framework.db.ds.LocalShopListDS
import org.koin.dsl.module

val repos = module {
    single { LocalShopItemDS(get()) }
    single { ShopItemRepo(get<LocalShopItemDS>()) }
    single { LocalShopListDS(get()) }
    single { ShopListRepo(get<LocalShopListDS>()) }
}
package io.github.xtoolkit.butler.shopping.framework.di

import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopItemRepo
import io.github.xtoolkit.butler.shopping.core.data.repositories.ShopListRepo
import io.github.xtoolkit.butler.shopping.framework.db.ds.LocalShopItemDS
import io.github.xtoolkit.butler.shopping.framework.db.ds.LocalShopListDS
import org.koin.dsl.module

val repos = module {
    single { LocalShopItemDS(get()) }
    single { ShopItemRepo(get<LocalShopItemDS>()) }
    single { LocalShopListDS(get()) }
    single { ShopListRepo(get<LocalShopListDS>(), get<LocalShopItemDS>()) }
}
package com.shopping.framework.di

import com.shopping.core.interactor.*
import org.koin.dsl.module

val useCases = module {
    single { AddShopItemUC(get()) }
    single { GetShopItemUC(get()) }
    single { GetAllShopItemUC(get()) }
    single { UpdateShopItemUC(get()) }
    single { DeleteShopItemUC(get()) }
    single { AddShopListUC(get()) }
    single { GetShopListUC(get()) }
    single { GetAllShopListUC(get()) }
    single { UpdateShopListUC(get()) }
}
package com.shopping.framework.di

import com.shopping.presentation.main.MainActivityViewModel
import com.shopping.presentation.shopitem.show.ShopItemShowFragmentViewModel
import com.shopping.presentation.shoplist.insert.ShopListInsertFragmentViewModel
import com.shopping.presentation.shoplist.show.ShopListShowFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get()) }
    viewModel { ShopListInsertFragmentViewModel(get(), get()) }
    viewModel { ShopListShowFragmentViewModel(get()) }
    viewModel { ShopItemShowFragmentViewModel(get(), get(), get(), get(), get()) }
}
package io.github.xtoolkit.butler.framework.di

import io.github.xtoolkit.butler.shopping.presentation.main.MainActivityViewModel
import io.github.xtoolkit.butler.shopping.presentation.shopitem.show.ShopItemShowFragmentViewModel
import io.github.xtoolkit.butler.shopping.presentation.shoplist.insert.ShopListInsertFragmentViewModel
import io.github.xtoolkit.butler.shopping.presentation.shoplist.manage.ShopListManageFragmentViewModel
import io.github.xtoolkit.butler.shopping.presentation.shoplist.show.ShopListShowFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get()) }
    viewModel { ShopListInsertFragmentViewModel(get(), get()) }
    viewModel { ShopListShowFragmentViewModel(get()) }
    viewModel { ShopListManageFragmentViewModel(get(), get()) }
    viewModel { ShopItemShowFragmentViewModel(get(), get(), get(), get(), get()) }
}
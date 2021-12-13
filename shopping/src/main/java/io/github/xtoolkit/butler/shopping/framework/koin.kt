package io.github.xtoolkit.butler.shopping.framework

import io.github.xtoolkit.butler.shopping.framework.di.fragments
import io.github.xtoolkit.butler.shopping.framework.di.repos
import io.github.xtoolkit.butler.shopping.framework.di.useCases
import io.github.xtoolkit.butler.shopping.framework.di.viewModelModule

val shoppingKoinModules = arrayOf(
    repos,
    useCases,
    viewModelModule,
    fragments,
)
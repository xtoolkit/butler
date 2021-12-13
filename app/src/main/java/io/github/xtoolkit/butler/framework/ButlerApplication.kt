package io.github.xtoolkit.butler.framework

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import io.github.xtoolkit.butler.framework.di.databaseModule
import io.github.xtoolkit.butler.shopping.framework.shoppingKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin

class ButlerApplication : Application() {
    @KoinExperimentalAPI
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ButlerApplication)
            fragmentFactory()
            modules(
                databaseModule,
                *shoppingKoinModules
            )
        }
    }
}
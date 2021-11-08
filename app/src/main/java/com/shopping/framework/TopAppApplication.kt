package com.shopping.framework

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.shopping.framework.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin

class TopAppApplication : Application() {
    @KoinExperimentalAPI
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TopAppApplication)
            fragmentFactory()
            modules(
                repos,
                useCases,
                databaseModule,
                viewModelModule,
                fragment
            )
        }
    }
}
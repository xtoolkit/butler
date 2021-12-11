package io.github.xtoolkit.butler.framework.di

import androidx.room.Room
import io.github.xtoolkit.butler.framework.db.TopAppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TopAppDatabase::class.java,
            "app_room.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<TopAppDatabase>().shopItemDao() }
    single { get<TopAppDatabase>().shopListDao() }
}
package io.github.xtoolkit.butler.framework.di

import androidx.room.Room
import io.github.xtoolkit.butler.framework.db.ButlerDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ButlerDatabase::class.java,
            "app_room.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<ButlerDatabase>().shopItemDao() }
    single { get<ButlerDatabase>().shopListDao() }
}
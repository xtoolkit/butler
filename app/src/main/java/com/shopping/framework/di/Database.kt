package com.shopping.framework.di

import androidx.room.Room
import com.shopping.framework.db.TopAppDatabase
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
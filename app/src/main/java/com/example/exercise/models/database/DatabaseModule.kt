package com.example.exercise.models.database

import com.example.exercise.models.database.config.getRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinDatabaseModule = module {
    single {
        getRoomDatabase(androidContext())
    }
}
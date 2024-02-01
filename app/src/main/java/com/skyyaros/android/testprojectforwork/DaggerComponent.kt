package com.skyyaros.android.testprojectforwork

import android.content.Context
import com.skyyaros.android.testprojectforwork.data.AppDatabase
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DaggerModule::class],
)
interface DaggerComponent {
    fun getDatabaseRepository(): DatabaseRepository
}

@Module
class DaggerModule(private val context: Context) {
    @Provides
    @Singleton
    fun appDatabase(): AppDatabase = AppDatabase.provide(context)

    @Provides
    @Singleton
    fun databaseRepository(appDatabase: AppDatabase): DatabaseRepository = DatabaseRepository(appDatabase)
}
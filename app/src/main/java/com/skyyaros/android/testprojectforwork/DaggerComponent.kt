package com.skyyaros.android.testprojectforwork

import android.content.Context
import com.skyyaros.android.testprojectforwork.data.AppDatabase
import com.skyyaros.android.testprojectforwork.data.DatabaseRepository
import com.skyyaros.android.testprojectforwork.data.InternetApi
import com.skyyaros.android.testprojectforwork.data.InternetRepository
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
    fun internetRepository(): InternetRepository
}

@Module
class DaggerModule(private val context: Context) {
    @Provides
    @Singleton
    fun appDatabase(): AppDatabase = AppDatabase.provide(context)

    @Provides
    @Singleton
    fun databaseRepository(appDatabase: AppDatabase): DatabaseRepository = DatabaseRepository(appDatabase)

    @Provides
    @Singleton
    fun internetApi(): InternetApi = InternetApi.provide()

    @Provides
    @Singleton
    fun internetRepository(internetApi: InternetApi): InternetRepository = InternetRepository(internetApi)
}
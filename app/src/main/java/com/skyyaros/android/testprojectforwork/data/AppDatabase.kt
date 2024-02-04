package com.skyyaros.android.testprojectforwork.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import com.skyyaros.android.testprojectforwork.entity.UserInfo

@Database(entities = [UserInfo::class, ProductWithPhoto::class], version = 1)
@TypeConverters(MyTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUsersDao(): UsersDao
    abstract fun getFavsDao(): FavDao

    companion object RoomDbProvider {
        private const val DATABASE_NAME = "ApplicationDb"

        fun provide(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
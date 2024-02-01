package com.skyyaros.android.testprojectforwork.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skyyaros.android.testprojectforwork.entity.UserInfo

@Database(entities = [UserInfo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUsersDao(): UsersDao

    companion object RoomDbProvider {
        private const val DATABASE_NAME = "ApplicationDb"

        fun provide(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
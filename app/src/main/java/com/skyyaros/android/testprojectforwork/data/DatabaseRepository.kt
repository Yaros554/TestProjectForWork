package com.skyyaros.android.testprojectforwork.data

import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.Flow

class DatabaseRepository(private val appDatabase: AppDatabase) {
    fun getUsers(): Flow<List<UserInfo>> {
        return appDatabase.getUsersDao().getUsers()
    }

    suspend fun addUser(newUser: UserInfo) {
        appDatabase.getUsersDao().addUser(newUser)
    }
}
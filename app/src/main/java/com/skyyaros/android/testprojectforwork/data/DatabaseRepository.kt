package com.skyyaros.android.testprojectforwork.data

import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.Flow

class DatabaseRepository(private val appDatabase: AppDatabase) {
    fun getUsers(): Flow<List<UserInfo>> {
        return appDatabase.getUsersDao().getUsers()
    }

    suspend fun addUser(newUser: UserInfo) {
        appDatabase.getUsersDao().addUser(newUser)
    }

    fun getFav(): Flow<List<ProductWithPhoto>> {
        return appDatabase.getFavsDao().getFav()
    }

    fun getFavId(): Flow<List<String>> {
        return appDatabase.getFavsDao().getFavId()
    }

    suspend fun addFav(newFav: ProductWithPhoto) {
        appDatabase.getFavsDao().addFav(newFav)
    }

    suspend fun deleteFav(fav: ProductWithPhoto) {
        appDatabase.getFavsDao().deleteFav(fav)
    }
}
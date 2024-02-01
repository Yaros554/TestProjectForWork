package com.skyyaros.android.testprojectforwork.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserInfo>>

    @Insert
    suspend fun addUser(newUser: UserInfo)
}
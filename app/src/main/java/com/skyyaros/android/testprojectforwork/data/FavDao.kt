package com.skyyaros.android.testprojectforwork.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyyaros.android.testprojectforwork.entity.ProductWithPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDao {
    @Query("SELECT id FROM FAVOURITES")
    fun getFavId(): Flow<List<String>>

    @Query("SELECT * FROM FAVOURITES")
    fun getFav(): Flow<List<ProductWithPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFav(fav: ProductWithPhoto)

    @Delete
    suspend fun deleteFav(fav: ProductWithPhoto)
}
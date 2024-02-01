package com.skyyaros.android.testprojectforwork.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserInfo(
    @ColumnInfo(name= "firstName") val firstName: String,
    @ColumnInfo(name= "lastName") val lastName: String,
    @PrimaryKey @ColumnInfo(name= "phone") val phone: String
)

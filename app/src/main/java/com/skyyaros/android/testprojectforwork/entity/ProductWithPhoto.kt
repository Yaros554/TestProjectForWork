package com.skyyaros.android.testprojectforwork.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Favourites")
data class ProductWithPhoto(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String,
    @ColumnInfo(name = "price") val price: Price,
    @ColumnInfo(name = "feedback") val feedback: Feedback?,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "available") val available: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "info") val listDopInfo: List<DopInfo>,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "imgUrl") val imgUrl: List<String>
): Parcelable

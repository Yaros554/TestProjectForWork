package com.skyyaros.android.testprojectforwork.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skyyaros.android.testprojectforwork.entity.DopInfo
import com.skyyaros.android.testprojectforwork.entity.Feedback
import com.skyyaros.android.testprojectforwork.entity.Price

class MyTypeConverter {
    @TypeConverter
    fun toPrice(value: String): Price {
        val priceType = object : TypeToken<Price>() {}.type
        return Gson().fromJson(value, priceType)
    }

    @TypeConverter
    fun fromPrice(price: Price): String {
        val gson = Gson()
        return gson.toJson(price)
    }

    @TypeConverter
    fun toFeedback(value: String?): Feedback? {
        val feedbackType = object : TypeToken<Feedback?>() {}.type
        return Gson().fromJson(value, feedbackType)
    }

    @TypeConverter
    fun fromFeedback(feedback: Feedback?): String? {
        val gson = Gson()
        return gson.toJson(feedback)
    }

    @TypeConverter
    fun toDopInfoList(value: String): List<DopInfo> {
        val listType = object : TypeToken<List<DopInfo>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDopInfoList(list: List<DopInfo>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
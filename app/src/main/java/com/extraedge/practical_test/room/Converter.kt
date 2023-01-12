package com.extraedge.practical_test.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun fromCountryLangList(value: ImageData): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCountryLangList(value: String): ImageData? {
        val gson = Gson()
//        val type = object : TypeToken<List<ImageData>>() {}.type
        return gson.fromJson(value, ImageData::class.java)
    }


}
package com.meuus90.base.arch.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class StringListTypeConverter {
    @TypeConverter
    fun listToString(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToList(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
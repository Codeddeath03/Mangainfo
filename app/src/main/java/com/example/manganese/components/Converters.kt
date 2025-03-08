package com.example.manganese.components

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string: String?): List<String>? {
        return if (string == null) null else Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)
    }
}

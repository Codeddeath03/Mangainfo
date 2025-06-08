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
        if (string == null) return null

        return try {
            Gson().fromJson(
                string.replace("'", "\""),
                object : TypeToken<List<String>>() {}.type
            )
        } catch (e: Exception) {
            null
        }
    }

}

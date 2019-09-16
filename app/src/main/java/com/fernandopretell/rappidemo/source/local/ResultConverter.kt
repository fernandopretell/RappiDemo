package com.fernandopretell.rappidemo.source.local

import androidx.room.TypeConverter
import com.fernandopretell.rappidemo.model.Pelicula
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.Collections.emptyList




class ResultConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToObjectList(data: String?): List<Pelicula> {
        if (data == null) return emptyList()

        val listType = object : TypeToken<List<Pelicula>>() {}.type

        return gson.fromJson<List<Pelicula>>(data, listType)
    }

    @TypeConverter
    fun objectListToString(list: List<Pelicula>): String? {
        return gson.toJson(list)
    }
}
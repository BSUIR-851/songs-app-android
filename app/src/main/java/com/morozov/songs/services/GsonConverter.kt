package com.morozov.songs.services

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonConverter {
    val converterGson = Gson()

    inline fun <reified T> toMap(v: T) : Map<String, Any>? {
        try {
            return converterGson.fromJson(toStr(v), object : TypeToken<Map<String, Any>>() {}.type)
        } catch (err : Throwable) {
            println(err.localizedMessage)
            return null
        }
    }

    inline fun <reified T> toObj(string: String?): T? {
        try {
            return converterGson.fromJson(string, object : TypeToken<T>() {}.type)
        } catch (err : Throwable) {
            println(err.localizedMessage)
            return null
        }
    }

    fun <T> toStr(o: T): String {
        return converterGson.toJson(o)
    }
}
package com.example.exercise.common.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

inline fun <reified T> String?.jsonToObject(): T? {
    if (this == null) {
        return null
    }
    return try {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        gson.fromJson(this, object : TypeToken<T>() {}.type)
    } catch (e: JsonSyntaxException) {
        null
    }
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}
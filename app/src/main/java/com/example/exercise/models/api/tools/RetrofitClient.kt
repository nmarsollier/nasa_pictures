package com.example.exercise.models.api.tools

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    companion object {
        private val gson = GsonBuilder().setLenient().create()

        private val okHttpClient = OkHttpClient.Builder()
            .let {
                it.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                )
            }
            .build()

        fun retrofit(base: String): Retrofit = Retrofit.Builder()
            .baseUrl(base)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}

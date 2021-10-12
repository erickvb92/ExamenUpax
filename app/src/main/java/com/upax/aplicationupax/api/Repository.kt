package com.upax.aplicationupax.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.upax.aplicationupax.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Repository {

    companion object {

        private fun createClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(Interceptor())
            return builder.build()
        }

        fun getApi(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

    fun getRepositoryApi(): API =
        getApi().create(API::class.java)

    }
}
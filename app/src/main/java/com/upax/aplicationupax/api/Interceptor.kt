package com.upax.aplicationupax.api

import okhttp3.Interceptor
import okhttp3.Response

const val API_KEY = "45bf6592c14a965b33549f4cc7e6c664"
class Interceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}
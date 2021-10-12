package com.upax.aplicationupax.api

import com.upax.aplicationupax.model.Response_moviedb
import retrofit2.Response
import retrofit2.http.GET

private const val ENDPOINT = "movie"


interface API {

    @GET("$ENDPOINT/popular")
    suspend fun getRequestPopular(): Response<Response_moviedb>

    @GET("$ENDPOINT/now_playing")
    suspend fun getRequestNow(): Response<Response_moviedb>

}
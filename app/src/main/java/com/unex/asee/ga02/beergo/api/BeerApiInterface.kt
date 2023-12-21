package com.unex.asee.ga02.beergo.api

import com.unex.asee.ga02.beergo.data.api.BeerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val service : BeerApiInterface by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.punkapi.com/v2/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(BeerApiInterface::class.java)
}

fun getNetworkService() = service

class APIError(message: String, cause: Throwable) : Exception(message)

interface BeerApiInterface {
    @GET("beers")
    fun getBeers(
        @Query("page") page: Int
    ): Call<List<BeerApi>>

    @GET("beer_details/{id}")
    fun getBeerDetails(@Query("id") id: Int): Call<BeerApi>


}
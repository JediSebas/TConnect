package com.jedisebas.tconnect.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("v1/products")
    fun getAll(): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v1/products/{code}")
    fun getByCode(@Path("code") code: Long): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v1/products/{numberT}/{date}")
    fun getByNumberTAndDate(@Path("numberT") numberT: Int, @Path("date") date: LocalDate): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v1/products/{numberT}/{date}/{wN}")
    fun getByNumberTAndDateAndWN(@Path("numberT") numberT: Int, @Path("date") date: LocalDate, @Path("wN") wN: String): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @PUT("v1/products")
    fun updateOne(@Body productDto: ProductDto): Call<ProductDto>
}
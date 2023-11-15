package com.jedisebas.tconnect.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("v1/products")
    fun getAll(): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v1/products/{code}")
    fun getByCode(@Path("code") code: Long): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @PUT("v1/products")
    fun updateOne(@Body productDto: ProductDto): Call<ProductDto>
}
package com.jedisebas.tconnect.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDate

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("v2/products")
    fun getAll(): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/without-t")
    fun getAllWithNullNumberT(): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/with-t")
    fun getAllWithNumberT(): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/main")
    fun getAllByCode(@Query("code") code: Long): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/main")
    fun getAllByCodePartAndNw(@Query("part") part: Long,@Query("nw") nw: Int): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/extra")
    fun getAllByParamsCode(@Query("code") code: Long, @Query("number") number: Int,
                           @Query("date") date: LocalDate, @Query("nw") nw: Int): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @GET("v2/products/extra")
    fun getAllByParamsCodePart(@Query("part") part: Long, @Query("number") number: Int,
                           @Query("date") date: LocalDate, @Query("nw") nw: Int): Call<List<ProductDto>>

    @Headers("Content-Type: application/json")
    @PUT("v2/products")
    fun updateOne(@Body productDto: ProductDto): Call<ProductDto>
}
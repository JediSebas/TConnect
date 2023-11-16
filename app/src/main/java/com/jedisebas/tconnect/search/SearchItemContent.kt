package com.jedisebas.tconnect.search

import com.jedisebas.tconnect.api.ApiClient
import com.jedisebas.tconnect.api.ProductDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchItemContent {

    val ITEMS: MutableList<SearchItem> = ArrayList()

    val ITEM_MAP: MutableMap<Int, SearchItem> = HashMap()

    init {
        insertItems()
    }

    private fun insertItems() {
        val api = ApiClient.createApi()
        val call = api.getByCode(5900592341879)

        call.enqueue(object : Callback<List<ProductDto>> {
            override fun onResponse(
                call: Call<List<ProductDto>>,
                response: Response<List<ProductDto>>
            ) {
                if (response.isSuccessful) {
                    val products = response.body()
                    var counter = 0
                    products?.let {
                        for (product in it) {
                            val item = SearchItem(counter, product.code.toString(),
                                product.nW.toString(), product.wN)
                            ITEMS.add(item)
                            ITEM_MAP.put(item.id, item)
                            counter++
                        }
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ProductDto>>, t: Throwable) {
                println("Failed to obtain data. Error: ${t.message}")
            }
        })
    }

    data class SearchItem(val id: Int, val code: String, val nw: String, val wn: String) {
        override fun toString(): String = code
    }
}
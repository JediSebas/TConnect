package com.jedisebas.tconnect.search

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jedisebas.tconnect.api.ApiClient
import com.jedisebas.tconnect.api.ProductDto
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchItemViewModel : ViewModel() {

    val ITEMS: MutableList<SearchItem> = ArrayList()

    private val _dataList = MutableLiveData<List<SearchItem>>()
    val dataList: LiveData<List<SearchItem>> get() = _dataList

    fun insertItems(code: Long) {
        viewModelScope.launch {
            val api = ApiClient.createApi()
            val call = api.getAll() // TODO later change to getByCode

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
                                counter++
                            }
                        }
                        _dataList.postValue(ITEMS)
                    } else {
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ProductDto>>, t: Throwable) {
                    println("Failed to obtain data. Error: ${t.message}")
                }
            })
        }
    }

    @Parcelize
    data class SearchItem(val id: Int, val code: String, val nw: String, val wn: String) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        )

        companion object : Parceler<SearchItem> {

            override fun create(parcel: Parcel): SearchItem {
                return SearchItem(parcel)
            }

            override fun SearchItem.write(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(code)
                parcel.writeString(nw)
                parcel.writeString(wn)
            }
        }
    }
}
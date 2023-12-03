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

    private val _dataList = MutableLiveData<List<SearchItem>>()
    val dataList: LiveData<List<SearchItem>> get() = _dataList

    private val _isNotConnected = MutableLiveData<Boolean>()
    val isNotConnected: LiveData<Boolean> get() = _isNotConnected

    fun insertItems(flags: Int, code: Long, part: Long, nw: Int) {
        viewModelScope.launch {
            _isNotConnected.value = false
            val api = ApiClient.createApi()
            val call: Call<List<ProductDto>> = when (flags) {
                SearchFragment.CODE_SEARCH -> {
                    api.getAllByCode(code)
                }
                SearchFragment.PART_SEARCH -> {
                    api.getAllByCodePartAndNw(part, nw)
                }
                else -> {
                    api.getAllWithNullNumberT()
                }
            }

            _dataList.postValue(ArrayList())

            call.enqueue(object : Callback<List<ProductDto>> {
                override fun onResponse(
                    call: Call<List<ProductDto>>,
                    response: Response<List<ProductDto>>
                ) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        var counter = 0
                        val items: MutableList<SearchItem> = ArrayList()
                        products?.let {
                            for (product in it) {
                                val item = SearchItem(counter, product.name, product.code.toString(),
                                    product.nW.toString(), product.wN)
                                items.add(item)
                                counter++
                            }
                        }
                        _dataList.postValue(items)
                    } else {
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ProductDto>>, t: Throwable) {
                    _isNotConnected.postValue(true)
                    println("Failed to obtain data. Error: ${t.message}")
                }
            })
        }
    }

    @Parcelize
    data class SearchItem(val id: Int, val name: String, val code: String, val nw: String, val wn: String) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
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
                parcel.writeString(name)
                parcel.writeString(code)
                parcel.writeString(nw)
                parcel.writeString(wn)
            }
        }
    }
}
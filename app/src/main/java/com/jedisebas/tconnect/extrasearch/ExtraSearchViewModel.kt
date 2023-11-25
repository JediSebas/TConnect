package com.jedisebas.tconnect.extrasearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jedisebas.tconnect.api.ApiClient
import com.jedisebas.tconnect.api.ProductDto
import com.jedisebas.tconnect.search.SearchItemViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

object ExtraSearchViewModel : ViewModel() {

    private val _dataList = MutableLiveData<List<SearchItemViewModel.SearchItem>>()
    val dataList: LiveData<List<SearchItemViewModel.SearchItem>> get() = _dataList

    private val _isNotConnected = MutableLiveData<Boolean>()
    val isNotConnected: LiveData<Boolean> get() = _isNotConnected

    fun insertItems(numberT: Int, date: LocalDate, wN: String) {
        viewModelScope.launch {
            _isNotConnected.value = false
            val api = ApiClient.createApi()
            val call = if (wN.isEmpty()) {
                api.getByNumberTAndDate(numberT, date)
            } else {
                api.getByNumberTAndDateAndWN(numberT, date, wN)
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
                        val items: MutableList<SearchItemViewModel.SearchItem> = ArrayList()
                        products?.let {
                            for (product in it) {
                                val item = SearchItemViewModel.SearchItem(
                                    counter, product.name, product.code.toString(),
                                    product.nW.toString(), product.wN
                                )
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
}
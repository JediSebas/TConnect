package com.jedisebas.tconnect.ticket

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jedisebas.tconnect.api.ApiClient
import com.jedisebas.tconnect.api.ProductDto
import com.jedisebas.tconnect.search.SearchItemViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class TicketViewModel : ViewModel() {


    private val _dataList = MutableLiveData<List<TicketItem>>()
    val dataList: LiveData<List<TicketItem>> get() = _dataList
    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> get() = _requestState

    fun insertNumbers() {
        viewModelScope.launch {
            val numbers: MutableList<TicketItem> = ArrayList()
            for (i in 1..30) {
                val item = TicketItem(i)
                numbers.add(item)
            }
            _dataList.postValue(numbers)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SimpleDateFormat")
    fun changeNumberT(ticket: TicketItem, searchItem: SearchItemViewModel.SearchItem?) {
        _requestState.value = RequestState.Loading
        GlobalScope.launch {
            val currentDate = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formattedDate = dateFormat.format(currentDate)

            val product: ProductDto? = searchItem?.let {
                ProductDto(
                    it.code.toLong(),
                    it.name,
                    it.nw.toInt(),
                    it.wn,
                    ticket.id,
                    formattedDate
                )
            }

            val api = ApiClient.createApi()

            if (product != null) {
                val call = api.updateOne(product)
                call.enqueue(object : Callback<ProductDto> {
                    override fun onResponse(call: Call<ProductDto>, response: Response<ProductDto>) {
                        if (response.isSuccessful) {
                            println("Successful")
                            _requestState.postValue(RequestState.Success("Zaktualizowano dane!"))
                        } else {
                            _requestState.postValue(RequestState.Error("Błąd połączenia, nic nie zostało zmienione"))
                            println("Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ProductDto>, t: Throwable) {
                        _requestState.postValue(RequestState.Error("Błąd połączenia, nic nie zostało zmienione"))
                        println("Mission failed. Error: ${t.message}")
                    }
                })
            }
        }
    }

    data class TicketItem(val id: Int)
}
package com.jedisebas.tconnect.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

object TicketViewModel : ViewModel() {


    private val _dataList = MutableLiveData<List<TicketItem>>()
    val dataList: LiveData<List<TicketItem>> get() = _dataList

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

    data class TicketItem(val id: Int)
}
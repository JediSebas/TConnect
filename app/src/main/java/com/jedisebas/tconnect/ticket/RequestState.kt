package com.jedisebas.tconnect.ticket

sealed class RequestState {
    data object Loading : RequestState()
    data class Success(val message: String) : RequestState()
    data class Error(val errorMessage: String) : RequestState()
}

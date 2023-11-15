package com.jedisebas.tconnect.api

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("Kod") var code: Long,
    @SerializedName("Nazwa") var name: String,
    @SerializedName("N_W") var nW: Int,
    @SerializedName("W_N") var wN: String,
    @SerializedName("NumerT") var numberT: Int?,
    @SerializedName("Data Godzina Z") var dateTime: String?
)

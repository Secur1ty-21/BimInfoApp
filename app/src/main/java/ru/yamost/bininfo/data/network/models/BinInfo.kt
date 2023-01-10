package ru.yamost.bininfo.data.network.models

import com.squareup.moshi.Json

data class BinInfo(
    val country: Country,
    val bank: Bank,
    @Json(name = "number")
    val cardNumber: CardNumber,
    val scheme: String,
    val type: String,
    val brand: String,
    @Json(name = "prepaid")
    val isPrepaid: Boolean
)
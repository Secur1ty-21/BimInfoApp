package ru.yamost.bininfo.data.network.models

import com.squareup.moshi.Json

data class CardNumber(
    val length: Int,
    @Json(name = "luhn")
    val isValidate: Boolean
)
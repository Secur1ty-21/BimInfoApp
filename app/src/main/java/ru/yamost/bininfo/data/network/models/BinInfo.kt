package ru.yamost.bininfo.data.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BinInfo(
    val country: Country,
    val bank: Bank,
    @Json(name = "number")
    val cardNumber: CardNumber,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    @Json(name = "prepaid")
    val isPrepaid: Boolean?
): Parcelable
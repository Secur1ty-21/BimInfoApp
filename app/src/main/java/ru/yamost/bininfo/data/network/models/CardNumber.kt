package ru.yamost.bininfo.data.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardNumber(
    val length: Int?,
    @Json(name = "luhn")
    val isValidate: Boolean?
): Parcelable
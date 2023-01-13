package ru.yamost.bininfo.data.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    val name: String?,
    val url: String?,
    val phone: String?,
    val city: String?
): Parcelable
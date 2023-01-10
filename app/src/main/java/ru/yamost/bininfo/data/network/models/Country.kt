package ru.yamost.bininfo.data.network.models

data class Country(
    val numeric: String,
    val alpha2: String,
    val name: String,
    val emoji: String,
    val currency: String,
    val latitude: Long,
    val longitude: Long
)
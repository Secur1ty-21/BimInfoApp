package ru.yamost.bininfo.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.yamost.bininfo.data.network.models.BinInfo

private const val BASE_URL = "https://lookup.binlist.net/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BinApiService {
    @GET("/{cardNumber}")
    suspend fun getBinInfo(@Path("cardNumber") cardNumber: String): BinInfo
}

object BinApi {
    val retrofitService: BinApiService by lazy {
        retrofit.create()
    }
}
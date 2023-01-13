package ru.yamost.bininfo.domain

import ru.yamost.bininfo.data.network.BinApi
import ru.yamost.bininfo.data.network.models.BinInfo


class GetBinInfoByCardNumber {
    suspend fun execute(cardNumber: String): BinInfo {
        return BinApi.retrofitService.getBinInfo(cardNumber)
    }
}
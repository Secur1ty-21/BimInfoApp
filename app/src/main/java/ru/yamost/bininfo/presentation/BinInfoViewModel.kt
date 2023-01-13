package ru.yamost.bininfo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yamost.bininfo.data.network.models.BinInfo
import ru.yamost.bininfo.domain.GetBinInfoByCardNumber
import java.util.InputMismatchException

enum class BinInfoApiStatus {
    LOADING, ERROR, DONE, DEFAULT
}

class BinInfoViewModel: ViewModel() {
    private val _status = MutableLiveData<BinInfoApiStatus>()
    val status: LiveData<BinInfoApiStatus> = _status
    private val _binInfoList = MutableLiveData<List<BinInfo>>()
    val binInfoList: LiveData<List<BinInfo>> = _binInfoList
    private val _binInfo = MutableLiveData<BinInfo>()
    val binInfo: LiveData<BinInfo> = _binInfo
    private var _error: Exception = Exception()
    val error: Exception
        get() = _error

    fun getBinDetailInfo(cardNumber: String) {
        if (cardNumber.isEmpty() || (cardNumber.length != 6 && cardNumber.length != 8)) {
            _error = InputMismatchException()
            _status.value = BinInfoApiStatus.ERROR
            _status.value = BinInfoApiStatus.DEFAULT
            return
        }
        viewModelScope.launch {
            _status.value = BinInfoApiStatus.LOADING
            try {
                _binInfo.value = GetBinInfoByCardNumber().execute(cardNumber)
                _status.value = BinInfoApiStatus.DONE
                _status.value = BinInfoApiStatus.DEFAULT
            } catch (exception: Exception) {
                _error = exception
                Log.d("checkTest1", exception.message.toString() + exception.localizedMessage + exception.stackTraceToString())
                _status.value = BinInfoApiStatus.ERROR
                _status.value = BinInfoApiStatus.DEFAULT
            }
        }
    }

    init {
        _status.value = BinInfoApiStatus.DEFAULT
    }
}
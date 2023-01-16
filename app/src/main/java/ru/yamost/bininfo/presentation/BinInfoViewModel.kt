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

class BinInfoViewModel : ViewModel() {
    private val _status = MutableLiveData<BinInfoApiStatus>()
    val status: LiveData<BinInfoApiStatus> = _status
    private val mapInstitute = mutableMapOf<String, BinInfo>()
    private val _binInfo = MutableLiveData<BinInfo>()
    val binInfo: LiveData<BinInfo> = _binInfo
    private val _searchHistory = mutableListOf<String>()
    val searchHistory: List<String> = _searchHistory
    private var _error: Exception = Exception()
    val error: Exception
        get() = _error

    fun getBinInstitute(cardNumber: String) {
        if (isInvalidInputCardNumber(cardNumber)) {
            _error = InputMismatchException()
            _status.value = BinInfoApiStatus.ERROR
            return
        }
        if (mapInstitute.containsKey(cardNumber)) {
            getInstituteFromStorage(cardNumber)
        } else {
            findInstituteFromService(cardNumber)
        }
    }

    private fun isInvalidInputCardNumber(cardNumber: String): Boolean {
        return cardNumber.isEmpty() || (cardNumber.length != 6 && cardNumber.length != 8)
    }

    private fun getInstituteFromStorage(cardNumber: String) {
        _binInfo.value = mapInstitute[cardNumber]
        _status.value = BinInfoApiStatus.DONE
    }

    private fun findInstituteFromService(cardNumber: String) {
        viewModelScope.launch {
            _status.value = BinInfoApiStatus.LOADING
            try {
                _binInfo.value = GetBinInfoByCardNumber().execute(cardNumber)
                _binInfo.value?.let { mapInstitute[cardNumber] = it }
                _searchHistory.add(cardNumber)
                _status.value = BinInfoApiStatus.DONE
            } catch (exception: Exception) {
                _error = exception
                Log.d(
                    "CheckRequest",
                    "${exception.localizedMessage}\n ${exception.message}\n ${exception.stackTraceToString()} "
                )
                _status.value = BinInfoApiStatus.ERROR
            }
        }
    }

    init {
        _status.value = BinInfoApiStatus.DEFAULT
    }

    fun setDefaultState() {
        _status.value = BinInfoApiStatus.DEFAULT
    }
}
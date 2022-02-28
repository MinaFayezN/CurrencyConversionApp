package dev.mina.currency.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.currency.SingleEvent
import dev.mina.currency.trigger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "ConverterViewModel"
private const val STARTING_VALUE = "1.0"

@HiltViewModel
class ConverterViewModel @Inject constructor(converterRepo: ConverterRepo) : ViewModel() {

    val from = MutableLiveData(STARTING_VALUE)
    var rate = MutableLiveData(BigDecimal(1.0))

    private val _loading = MutableLiveData(SingleEvent(false))
    val loading: LiveData<SingleEvent<Boolean>> = _loading

    init {
        Log.d(TAG, "init viewModel")
        _loading.trigger(true)
        viewModelScope.launch {
            val symbols = converterRepo.getSymbols()
            val latestRates = converterRepo.getLatestRates(symbols = listOf("GBP", "JPY", "EUR"))
            val convert = converterRepo.convert()
            delay(3000)
            rate.postValue(latestRates.rates?.get("GBP"))
            _loading.trigger(false)
            Log.d(TAG, symbols.toString())
            Log.d(TAG, latestRates.toString())
            Log.d(TAG, convert.toString())
        }
    }
}
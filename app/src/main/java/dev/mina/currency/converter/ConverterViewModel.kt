package dev.mina.currency.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ConverterViewModel"

@HiltViewModel
class ConverterViewModel @Inject constructor(converterRepo: ConverterRepo) : ViewModel() {

    init {
        Log.d(TAG, "init viewModel")
        viewModelScope.launch {
            val symbols = converterRepo.getSymbols()
            val latestRates = converterRepo.getLatestRates(symbols = listOf("GBP", "JPY", "EUR"))
            Log.d(TAG, symbols.toString())
            Log.d(TAG, latestRates.toString())
        }
    }
}
package dev.mina.currency.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.currency.SingleEvent
import dev.mina.currency.data.LatestRates
import dev.mina.currency.trigger
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

private const val TAG = "ConverterViewModel"

@HiltViewModel
class ConverterViewModel @Inject constructor(private val converterRepo: ConverterRepo) :
    ViewModel() {

    private val _rate = MutableLiveData(BigDecimal(1.0))
    val rate: LiveData<BigDecimal> = _rate

    private val _fromSymbols = MutableLiveData<LinkedList<String>>()
    val fromSymbols: LiveData<LinkedList<String>> = _fromSymbols

    private val _toSymbols = MutableLiveData<LinkedList<String>>()
    val toSymbols: LiveData<LinkedList<String>> = _toSymbols

    private val _selectedFromLD = MutableLiveData<SingleEvent<Int>>()
    val selectedFromLD: LiveData<SingleEvent<Int>> = _selectedFromLD

    private val _selectedToLD = MutableLiveData<SingleEvent<Int>>()
    val selectedToLD: LiveData<SingleEvent<Int>> = _selectedToLD

    private var selectedFrom = 0 //Update Index base to be String Base
    private var selectedTo = 0

    private val _loading = MutableLiveData(SingleEvent(false))
    val loading: LiveData<SingleEvent<Boolean>> = _loading

    private var latestRates: LatestRates? = null
    private var symbols: LinkedList<String>? = null

    init {
        Log.d(TAG, "init viewModel")
        _loading.trigger(true)
        viewModelScope.launch {
            updateSymbols()
            updateRates()
            publishRate()
            _loading.trigger(false)
        }
    }

    private suspend fun updateSymbols() {
        symbols = converterRepo.getSymbols().symbols?.let { LinkedList(it.keys) }?.also {
            updateFromList(it)
            updateToList(it)
        }
    }

    private fun updateFromList(newSymbols: LinkedList<String>) {
        _fromSymbols.postValue(LinkedList(newSymbols))
    }

    private fun updateToList(newSymbols: LinkedList<String>) {
        _toSymbols.postValue(LinkedList(newSymbols).apply {
            remove(newSymbols[selectedFrom])
        })
    }

    private suspend fun updateRates(base: String? = _fromSymbols.value?.get(selectedFrom)) {
        latestRates = converterRepo.getLatestRates(base = base,
            symbols = symbols)
    }

    private fun publishRate() {
        latestRates?.rates?.get(_toSymbols.value?.get(selectedTo))?.let(_rate::postValue)
    }

    fun swap() {
        viewModelScope.launch {
            _loading.trigger(true)
            val newFromBase = _toSymbols.value?.get(selectedTo)
            val newToSelection = _fromSymbols.value?.get(selectedFrom)
            selectedFrom = _fromSymbols.value?.indexOf(newFromBase) ?: 0
            symbols?.let {
                _toSymbols.postValue(LinkedList(it).apply {
                    remove(newFromBase)
                }.also { newList ->
                    selectedTo = newList.indexOf(newToSelection)
                })
            }
            _selectedFromLD.trigger(selectedFrom)
            _selectedToLD.trigger(selectedTo)
            updateRates()
            publishRate()
            _loading.trigger(false)
        }
    }

    fun updateSelected(type: SelectedType, position: Int) {
        viewModelScope.launch {
            when (type) {
                SelectedType.FROM -> {
                    _loading.trigger(true)
                    selectedFrom = position
                    symbols?.let(this@ConverterViewModel::updateToList)
                    updateRates()
                }
                SelectedType.TO -> {
                    selectedTo = position
                }
            }
            publishRate()
            _loading.trigger(false)
        }
    }
}
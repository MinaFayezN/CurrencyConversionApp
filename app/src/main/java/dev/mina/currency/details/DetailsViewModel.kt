package dev.mina.currency.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.currency.data.HistoricRate
import dev.mina.currency.utils.SingleEvent
import dev.mina.currency.utils.trigger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepo: DetailsRepo,
    private val state: SavedStateHandle,
) : ViewModel() {

    private val _loading = MutableLiveData(SingleEvent(false))
    val loading: LiveData<SingleEvent<Boolean>> = _loading

    private val _historicalRates = MutableLiveData(HistoricRate())
    val historicalRates: LiveData<HistoricRate> = _historicalRates

    private val _timeSeries = MutableLiveData<List<HistoricItem>>()
    val timeSeries: LiveData<List<HistoricItem>> = _timeSeries


    init {
        _loading.trigger(true)
        viewModelScope.launch(Dispatchers.IO) {
            val base = state.get<String>("base")
            val to = state.get<String>("to")
            val symbols = state.get<String?>("symbols")
            val resp = detailsRepo.getTimeSeriesRates(base = base, symbols = symbols)
            resp.rates?.keys?.map {
                HistoricItem(date = it,
                    fromText = base,
                    fromRate = "1.0",
                    toText = to,
                    toRate = "${resp.rates[it]?.get(to) ?: BigDecimal(0)}"
                )
            }?.also {
                _timeSeries.postValue(it)
            }
            _loading.trigger(false)
        }
    }
}
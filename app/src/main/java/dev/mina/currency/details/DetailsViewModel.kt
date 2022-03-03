package dev.mina.currency.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.currency.data.HistoricRate
import dev.mina.currency.data.TimeSeriesRates
import dev.mina.currency.utils.SingleEvent
import dev.mina.currency.utils.trigger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private val _timeSeries = MutableLiveData(TimeSeriesRates())
    val timeSeries: LiveData<TimeSeriesRates> = _timeSeries


    init {
        _loading.trigger(true)
        viewModelScope.launch(Dispatchers.IO) {
            val base = state.get<String>("base")
            val symbols = state.get<String?>("symbols")
            val resp = detailsRepo.getTimeSeriesRates(base = base, symbols = symbols)
            _timeSeries.postValue(resp)
            _loading.trigger(false)
        }
    }
}
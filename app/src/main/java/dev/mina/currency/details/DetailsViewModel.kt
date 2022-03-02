package dev.mina.currency.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.currency.utils.SingleEvent
import dev.mina.currency.utils.trigger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val converterRepo: DetailsRepo,
    private val state: SavedStateHandle,
) :
    ViewModel() {


    private val _loading = MutableLiveData(SingleEvent(false))
    val loading: LiveData<SingleEvent<Boolean>> = _loading


    init {
        _loading.trigger(true)
        viewModelScope.launch {
            delay(3000)
            state.get<String>("base")
            _loading.trigger(false)
        }
    }
}
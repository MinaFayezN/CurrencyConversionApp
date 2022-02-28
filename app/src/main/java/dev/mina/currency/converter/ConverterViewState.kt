package dev.mina.currency.converter

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import dev.mina.currency.divideBy
import dev.mina.currency.multiplyBy
import java.math.BigDecimal

class ConverterViewState(base: MutableLiveData<String>) : BaseObservable() {
    val itemsDisabled = ObservableBoolean(false)

    private var rate = BigDecimal(1.0)
    val from = base
    val to = MutableLiveData<String>()

    val convertFrom: (String) -> Unit = { to.postValue(it multiplyBy rate) }
    val convertTo: (String) -> Unit = { from.postValue(it divideBy rate) }

    fun updateRate(newRate: BigDecimal) {
        this.rate = newRate
        from.value?.let(convertFrom)
    }
}


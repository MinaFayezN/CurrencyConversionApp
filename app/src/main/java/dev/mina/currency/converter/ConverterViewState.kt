package dev.mina.currency.converter

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import dev.mina.currency.divideBy
import dev.mina.currency.multiplyBy
import java.math.BigDecimal

class ConverterViewState : BaseObservable() {

    private val rate = BigDecimal(1.0)

    val startingValue = ObservableField("1.0")
    val from = ObservableField<String>(startingValue)
    val to = ObservableField<String>()

    val convertFrom: (String) -> Unit = { to.set(it multiplyBy rate) }
    val convertTo: (String) -> Unit = { from.set(it divideBy rate) }
}


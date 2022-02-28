package dev.mina.currency.converter

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dev.mina.currency.ItemSelectedListener
import dev.mina.currency.divideBy
import dev.mina.currency.multiplyBy
import java.math.BigDecimal
import java.util.*

private const val STARTING_VALUE = "1.0"

class ConverterViewState(
    onSelectionChanged: (SelectedType, Int) -> Unit,
    private val onSwapClick: () -> Unit,
) :
    BaseObservable() {

    private var rate = BigDecimal(STARTING_VALUE)

    @Bindable
    val itemsDisabled = ObservableBoolean(false)

    @Bindable
    val from = MutableLiveData(STARTING_VALUE)

    @Bindable
    val to = MutableLiveData<String>()

    @Bindable
    val convertFrom: (String) -> Unit = { to.postValue(it multiplyBy rate) }

    @Bindable
    val convertTo: (String) -> Unit = { from.postValue(it divideBy rate) }

    @Bindable
    val fromSymbols = ObservableField<LinkedList<String>>()

    @Bindable
    val toSymbols = ObservableField<LinkedList<String>>()

    @Bindable
    val selectedFrom = ObservableField<Int>()

    @Bindable
    val selectedTo = ObservableField<Int>()

    @Bindable
    val onToItemChanged = object : ItemSelectedListener {
        override fun onItemSelected(position: Int) {
            onSelectionChanged.invoke(SelectedType.TO, position)
        }
    }

    @Bindable
    val onFromItemChanged = object : ItemSelectedListener {
        override fun onItemSelected(position: Int) {
            onSelectionChanged.invoke(SelectedType.FROM, position)
        }
    }

    fun onSwapClick(view: View) {
        onSwapClick.invoke()
    }

    fun updateRate(newRate: BigDecimal) {
        this.rate = newRate
        from.value?.let(convertFrom)
    }
}

enum class SelectedType {
    FROM,
    TO
}
package dev.mina.currency.converter

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dev.mina.currency.R
import dev.mina.currency.divideBy
import dev.mina.currency.multiplyBy
import dev.mina.currency.ui.ItemSelectedListener
import java.math.BigDecimal
import java.util.*

private const val STARTING_VALUE = "1.0"

class ConverterViewState(
    onSelectionChanged: (SelectedType, String) -> Unit,
    private val onSwapClick: () -> Unit,
    private val onDetailsClick: () -> Unit,
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
            onSelectionChanged.invoke(SelectedType.TO, toSymbols.get()?.get(position) ?: "")
            selectedTo.set(position)
        }
    }

    @Bindable
    val onFromItemChanged = object : ItemSelectedListener {
        override fun onItemSelected(position: Int) {
            onSelectionChanged.invoke(SelectedType.FROM, fromSymbols.get()?.get(position) ?: "")
            selectedFrom.set(position)
        }
    }

    fun onButtonClick(view: View) {
        when (view.id) {
            R.id.ib_swap -> onSwapClick.invoke()
            R.id.btn_details -> onDetailsClick.invoke()
        }
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
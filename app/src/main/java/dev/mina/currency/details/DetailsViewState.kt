package dev.mina.currency.details

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import dev.mina.currency.R
import dev.mina.currency.databinding.HistoricItemBinding
import dev.mina.currency.databinding.OthersItemBinding
import dev.mina.currency.ui.adapters.GenericListAdapter

class DetailsViewState : BaseObservable() {

    @Bindable
    val itemsDisabled = ObservableBoolean(false)

    @Bindable
    val historicAdapter = GenericListAdapter<HistoricItemBinding, HistoricItem>(
        layoutId = R.layout.historic_item,
        setViewState = { it.viewState = HistoricalItemViewState() },
        bindData = { binding, data, _ -> binding.viewState?.update(data) },
        isSameID = { old, new -> old.fromText == new.fromText && old.toText == new.toText },
        isContent = { old, new -> old.fromRate == new.fromRate && old.toRate == new.toRate },
    )

    @Bindable
    val othersAdapter = GenericListAdapter<OthersItemBinding, String>(
        layoutId = R.layout.others_item,
        setViewState = { it.viewState = OthersItemViewState() },
        bindData = { binding, data, _ -> binding.viewState?.update(data) },
        isSameID = { old, new -> old == new },
        isContent = { old, new -> old == new },
    )

}


class HistoricalItemViewState : BaseObservable() {
    @Bindable
    var date = ObservableField<String>()

    @Bindable
    var fromText = ObservableField<String>()

    @Bindable
    var fromRate = ObservableField<String>()

    @Bindable
    var toText = ObservableField<String>()

    @Bindable
    var toRate = ObservableField<String>()

    fun update(data: HistoricItem) {
        date.set(data.date)
        fromText.set(data.fromText)
        fromRate.set(data.fromRate)
        toText.set(data.toText)
        toRate.set(data.toRate)
    }
}

class OthersItemViewState : BaseObservable() {

    @Bindable
    var date = ObservableField<String>()

    @Bindable
    var type = ObservableField<TextType>()

    fun update(data: String) {
        if (data.contains("Date: ")) {
            type.set(TextType.TITLE)
        } else {
            type.set(TextType.RATE)
        }
        date.set(data)
    }
}

enum class TextType {
    TITLE,
    RATE
}

data class HistoricItem(
    val date: String? = null,
    val fromText: String? = null,
    val fromRate: String? = null,
    val toText: String? = null,
    val toRate: String? = null,
)
package dev.mina.currency.details

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean

class DetailsViewState : BaseObservable(){

    @Bindable
    val itemsDisabled = ObservableBoolean(false)
}
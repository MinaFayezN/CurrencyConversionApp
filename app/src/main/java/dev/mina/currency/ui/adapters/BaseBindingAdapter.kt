package dev.mina.currency.ui.adapters

import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView Adapter that will handle DataBinding ViewHolders
 *
 * If we will have only one type of views in the adapter, then we must pass the Binding class to the adapter.
 *
 *      <em>class OneViewTypeAdapter : BaseBindingAdapter<GeneratedLayoutBindClass>()</em>
 *
 * When we have more than one kind of view types, then we send ViewDataBinding as the generic type for the adapter.
 *
 *      <em>class MultipleViewTypesAdapter : BaseBindingAdapter<ViewDataBinding>()</em>
 */
abstract class BaseBindingAdapter<T : ViewDataBinding> :
    RecyclerView.Adapter<BindingViewHolder<T>>() {

    abstract fun onCreateBindingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): T

    abstract fun onBindViewStateHolder(binding: T, position: Int, viewType: Int)

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> =
        BindingViewHolder.create(onCreateBindingViewHolder(from(parent.context), parent, viewType))

    final override fun onBindViewHolder(holder: BindingViewHolder<T>, position: Int) =
        onBindViewStateHolder(holder.binding, position, getItemViewType(position))
}

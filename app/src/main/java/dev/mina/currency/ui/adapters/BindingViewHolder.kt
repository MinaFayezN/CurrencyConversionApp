package dev.mina.currency.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Base binding view holder class to use with BaseBindingAdapter
 */
class BindingViewHolder<T : ViewDataBinding> private constructor(val binding: T) : BaseViewHolder(binding.root) {

    companion object {
        fun <T : ViewDataBinding> create(parent: ViewGroup, @LayoutRes layoutRes: Int): BindingViewHolder<T> {
            val binding = DataBindingUtil.inflate<T>(LayoutInflater.from(parent.context), layoutRes, parent, false)
            return BindingViewHolder(binding)
        }

        fun <T : ViewDataBinding> create(viewDataBinding: T): BindingViewHolder<T> {
            return BindingViewHolder(viewDataBinding)
        }
    }
}

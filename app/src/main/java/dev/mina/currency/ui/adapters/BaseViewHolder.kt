package dev.mina.currency.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base view holder class to use with ReyclerView.Adapter.
 *
 * Use RecyclerView.ViewHolder.itemView object to get the root view for this holder.
 */
open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind() {}
}

package dev.mina.currency.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil


class GenericListAdapter<T : ViewDataBinding, D>(
    @LayoutRes val layoutId: Int,
    private val setViewState: (T) -> Unit,
    private val bindData: (T, D, Int) -> Unit,
    private val isSameID: ((oldData: D, newData: D) -> Boolean),
    private val isContent: ((oldData: D, newData: D) -> Boolean),
) : BaseBindingAdapter<T>() {
    private var list = emptyList<D>()

    override fun onCreateBindingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): T = inflate<T>(inflater, layoutId, parent, false).also { setViewState(it) }

    override fun onBindViewStateHolder(binding: T, position: Int, viewType: Int) =
        bindData(binding, list[position], position)

    override fun getItemCount() = list.size

    fun updateData(data: List<D>) {
        val oldList = list
        val diffUtil = GenericDiffUtilV2(
            oldData = oldList,
            newData = data,
            isSameId = { oldItem, newItem -> isSameID(oldItem, newItem) },
            isSameContent = { oldItem, newItem -> isContent(oldItem, newItem) }
        )
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = data
        diffResult.dispatchUpdatesTo(this)
    }
}

class GenericDiffUtilV2<T>(
    private val oldData: List<T>,
    private val newData: List<T>,
    private val isSameContent: (oldItem: T, newItem: T) -> Boolean =
        { oldItem, newItem -> oldItem == newItem },
    private val isSameId: (oldItem: T, newItem: T) -> Boolean,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isSameId(oldData[oldItemPosition], newData[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isSameContent(oldData[oldItemPosition], newData[newItemPosition])
    }
}

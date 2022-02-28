package dev.mina.currency

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner


/**
 * set spinner onItemSelectedListener listener
 */
fun Spinner.setSpinnerItemSelectedListener(listener: ItemSelectedListener?) {
    if (listener == null) {
        onItemSelectedListener = null
    } else {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                if (tag != position) {
                    listener.onItemSelected(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

/**
 * set spinner value
 */
fun Spinner.setSpinnerValue(position: Int) {
    if (adapter != null) {
        setSelection(position, false)
        tag = position
    }
}


interface ItemSelectedListener {
    fun onItemSelected(position: Int)
}

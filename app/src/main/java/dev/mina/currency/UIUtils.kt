package dev.mina.currency

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.ObservableField

object UIUtils {
    @BindingAdapter(value = ["textSource", "convert"])
    @JvmStatic
    fun setTextSource(
        editText: EditText, textSource: ObservableField<String>, convert: (String) -> Unit,
    ) {
        textSource.get()?.let {
            if (editText.hasFocus()) convert(it)
            if (it != editText.text.toString()) editText.setText(it)
        }
    }

    @InverseBindingAdapter(attribute = "textSource", event = "android:textAttrChanged")
    @JvmStatic
    fun getTextSource(editText: EditText) = editText.text.toString()
}
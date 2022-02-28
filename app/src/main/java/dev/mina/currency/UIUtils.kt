package dev.mina.currency

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.MutableLiveData

object UIUtils {
    @BindingAdapter(value = ["textSource", "convert"])
    @JvmStatic
    fun setTextSource(
        editText: EditText, textSource: MutableLiveData<String>, convert: (String) -> Unit,
    ) {
        textSource.value?.let {
            if (editText.hasFocus()) convert(it)
            if (it != editText.text.toString()) editText.setText(it)
        }
    }

    @InverseBindingAdapter(attribute = "textSource", event = "android:textAttrChanged")
    @JvmStatic
    fun getTextSource(editText: EditText) = editText.text.toString()
}
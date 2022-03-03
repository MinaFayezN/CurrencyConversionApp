package dev.mina.currency.ui

import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dev.mina.currency.details.TextType

object UIUtils {
    @BindingAdapter("textType")
    @JvmStatic
    fun setTextType(
        textView: TextView,
        textType: ObservableField<TextType>,
    ) {
        when (textType.get()) {
            TextType.TITLE -> {
                textView.gravity = Gravity.START
                textView.setTypeface(null, Typeface.BOLD)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }
            TextType.RATE -> {
                textView.gravity = Gravity.CENTER
                textView.setTypeface(null, Typeface.NORMAL)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }
            null -> {
                textView.visibility = View.GONE
            }
        }
    }


    @BindingAdapter(value = ["textSource", "convert"])
    @JvmStatic
    fun setTextSource(
        editText: EditText,
        textSource: MutableLiveData<String>,
        convert: (String) -> Unit,
    ) {
        textSource.value?.let {
            if (editText.hasFocus()) convert(it)
            if (it != editText.text.toString()) editText.setText(it)
        }
    }

    @InverseBindingAdapter(attribute = "textSource", event = "android:textAttrChanged")
    @JvmStatic
    fun getTextSource(editText: EditText) = editText.text.toString()

    @BindingAdapter("onItemSelected")
    @JvmStatic
    fun Spinner.setItemSelectedListener(itemSelectedListener: ItemSelectedListener?) {
        setSpinnerItemSelectedListener(itemSelectedListener)
    }

    @BindingAdapter("newPosition")
    @JvmStatic
    fun Spinner.setNewPosition(position: Int?) {
        position?.let(this::setSpinnerValue)
    }
}


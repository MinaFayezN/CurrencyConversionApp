package dev.mina.currency

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean

fun <T> MutableLiveData<SingleEvent<T>>.trigger(data: T) {
    postValue(SingleEvent(data))
}

fun <T> LiveData<SingleEvent<T>>.observeForSingleEvent(
    owner: LifecycleOwner,
    action: (data: T) -> Unit,
) {
    observe(owner) {
        it.getContentIfNotHandled()?.let(action)
    }
}

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class SingleEvent<out T>(private val content: T) {

    private val hasBeenHandled: AtomicBoolean = AtomicBoolean(false)

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled.compareAndSet(false, true)) content
        else null
    }
}
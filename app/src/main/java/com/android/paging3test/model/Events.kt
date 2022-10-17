package com.android.paging3test.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Event<T>(value: T) {
   private var _value: T? = value
   fun get(): T? = _value.also { _value = null }
}

fun <T> MutableLiveData<T>.share(): LiveData<T> = this

/**
 * Type aliases for live-data instances which contains event.
 */
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
typealias LiveEvent<T> = LiveData<Event<T>>
typealias EventListener<T> = (T) -> Unit

fun <T> MutableLiveEvent<T>.publishEvent(value: T) {
   this.value = Event(value)
}

fun <T> LiveEvent<T>.observeEvent(lifecycleOwner: LifecycleOwner, listener: EventListener<T>) {
   this.observe(lifecycleOwner) { event ->
      event.get()?.let { value ->
         listener(value)
      }
   }
}
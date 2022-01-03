package io.github.xtoolkit.butler.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.xtoolkit.butler.utils.event.Callback
import io.github.xtoolkit.butler.utils.event.Event
import io.github.xtoolkit.butler.utils.event.Trigger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel<EVENTS> : ViewModel() {
    private val events = mutableListOf<Event<EVENTS>>()
    private val savedTriggers = mutableListOf<Trigger<EVENTS>>()
    private val onceTriggers = mutableListOf<String>()

    @Suppress("UNCHECKED_CAST")
    fun <T> on(event: EVENTS, once: Boolean = false, callback: Callback<T>) {
        val item = Event(event, callback as Callback<Any?>, once)
        events.add(item)
        savedTriggers.find { it.event == event }?.let {
            executeTrigger(item, it.data, it.onceExecuteId)
        }
    }

    @JvmName("onUnit")
    fun on(event: EVENTS, callback: Callback<Unit?>) = on(event, false, callback)

    fun <T> once(event: EVENTS, callback: Callback<T>) = on(event, true, callback)

    @JvmName("onceUnit")
    fun once(event: EVENTS, callback: Callback<Unit?>) = on(event, true, callback)

    fun off(event: EVENTS, callback: Callback<Any?>) = events
        .filter { it.type == event && it.callback.hashCode() == callback.hashCode() }
        .forEach { events.remove(it) }

    private fun executeTrigger(event: Event<EVENTS>, data: Any? = null, onceExecuteId: String?) =
        viewModelScope.launch(Dispatchers.Main) {
            if (onceExecuteId != null) {
                onceTriggers.find { it == onceExecuteId }?.let { return@launch }
                onceTriggers.add(onceExecuteId)
            }
            event.callback(data)
            if (event.once) events.remove(event)
        }

    fun trigger(
        event: EVENTS,
        data: Any? = null,
        save: Boolean = false,
        onceExecuteId: String? = null
    ) {
        if (save) savedTriggers.add(Trigger(event, data, onceExecuteId))
        events.filter { it.type == event }.forEach { executeTrigger(it, data, onceExecuteId) }
    }

    fun reset() = events.clear()
}
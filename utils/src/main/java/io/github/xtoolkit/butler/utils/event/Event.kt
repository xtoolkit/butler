package io.github.xtoolkit.butler.utils.event

data class Event<EVENTS>(
    val type: EVENTS,
    val callback: Callback<Any?>,
    val once: Boolean = false
)
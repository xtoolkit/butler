package com.shopping.utils.event

data class Trigger<EVENTS>(
    val event: EVENTS,
    val data: Any?,
    val onceExecuteId: String?
)
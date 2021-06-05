package com.example.appbank.eventbus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.example.eventbus.MessageEvent

class EventBusModel : ViewModel()  {
    fun produceEvent(event: MessageEvent) {
        _events.tryEmit(event) // NOT suspending
    }

    private val _events = MutableSharedFlow<MessageEvent>() // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEventSus(event: MessageEvent) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}
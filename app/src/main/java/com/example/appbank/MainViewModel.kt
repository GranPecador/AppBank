package com.example.appbank

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.eventbus.EventBusModel
import org.example.eventbus.MessageEvent

class MainViewModel() : ViewModel() {

    init {
        viewModelScope.launch {
            EventBusModel.produceEventSus(MessageEvent.MessageAmount(0.0))
        }
    }

    private val _amount: MutableLiveData<Double> = MutableLiveData(0.1)
    val amount:LiveData<Double> = _amount
    val events = EventBusModel.events.asLiveData()

    fun getSum() {
        viewModelScope.launch {
            EventBusModel.produceEventSus(MessageEvent.MessageAmount(0.0))
        }
    }

     fun postAmount(amountNew: Double) {
        _amount.value = amountNew
    }
}

package com.example.appbank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appbank.eventbus.EventBusModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.eventbus.EventBusApp
import org.example.eventbus.MessageEvent
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var bus: EventBusApp = EventBusApp()
    private lateinit var model: EventBusModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        textView.text = "Hey brat"
        model = ViewModelProvider(this).get(EventBusModel::class.java)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    model.events.collect {
                        withContext(Dispatchers.Main) {
                            handleButton(it)
                        }
                    }
                }

            }
        }
        findViewById<Button>(R.id.depositButton).setOnClickListener {
            lifecycleScope.launch {
                model.produceEventSus(MessageEvent.MessageDeposit("DepositActivity"))
            }
        }
        findViewById<Button>(R.id.creditButton).setOnClickListener {
            lifecycleScope.launch {
                model.produceEventSus(MessageEvent.MessageCredit("CreditActivity"))
            }
        }

    }

    private fun handleButton(event: MessageEvent) {
        val intent = Intent()
        when (event) {
            is MessageEvent.MessageDeposit -> {
                intent.setClassName(this@MainActivity, "com.example.deposit.${event.message}")
            }
            is MessageEvent.MessageCredit -> {
                intent.setClassName(this@MainActivity, "com.example.credit.${event.message}")
            }
        }
        startActivity(intent)
    }
}


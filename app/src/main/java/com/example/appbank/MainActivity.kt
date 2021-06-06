package com.example.appbank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appbank.eventbus.EventBusViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.eventbus.EventBusModel
import org.example.eventbus.MessageEvent

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    private lateinit var viewModel: EventBusViewModel
    var amount = -100000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.amountAppBankTextView)
        viewModel = ViewModelProvider(this).get(EventBusViewModel::class.java)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    EventBusModel.events.collect {
                        withContext(Dispatchers.Main) {
                            handleButton(it)
                        }
                    }
                }

            }
        }
        findViewById<Button>(R.id.depositButton).setOnClickListener {
            lifecycleScope.launch {
                EventBusModel.produceEventSus(MessageEvent.MessageDeposit("DepositActivity"))
            }
        }
        findViewById<Button>(R.id.creditButton).setOnClickListener {
            lifecycleScope.launch {
                EventBusModel.produceEventSus(MessageEvent.MessageCredit("CreditActivity"))
            }
        }
        if (amount == -100000.0)
            lifecycleScope.launch {
                EventBusModel.produceEventSus(MessageEvent.MessageAmount(10000))
            }
    }

    private fun handleButton(event: MessageEvent) =
        when (event) {
            is MessageEvent.MessageDeposit -> {
                val intent = Intent()
                lifecycleScope.launch {
                    EventBusModel.produceEventSus(MessageEvent.MessageAmount(amount))
                }
                intent.setClassName(this@MainActivity, "com.example.deposit.${event.message}")
                startActivity(intent)
            }
            is MessageEvent.MessageCredit -> {
                val intent = Intent()
                intent.setClassName(this@MainActivity, "com.example.credit.${event.message}")
                startActivity(intent)
            }
            is MessageEvent.MessageAmount -> {
                amount = event.amount
                textView.text = "У тебя есть $amount"
            }
            else -> {}
        }

}






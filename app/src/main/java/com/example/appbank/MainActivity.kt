package com.example.appbank

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.eventbus.EventBusModel
import org.example.eventbus.MessageEvent


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.extras
        val name = arguments?.get("amount")?.toString()

        setContentView(R.layout.activity_main)


        textView = findViewById(R.id.amountAppBankTextView)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.amount.observe(this) {
            textView.text = "Баланс: $it"
        }
        viewModel.postAmount(name?.toDouble() ?: 10000.0)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                EventBusModel.events.collect {
                    withContext(Dispatchers.Main) {
                        Log.e("before handle", "")
                        handleMainEvent(it)
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
    }

    private fun handleMainEvent(event: MessageEvent) =
        when (event) {
            is MessageEvent.MessageDeposit -> {
                Log.e("avt depocit", "")

                val intent = Intent()
                intent.setClassName(this@MainActivity, "com.example.deposit.${event.message}")
                intent.putExtra("amount", viewModel.amount.value);
                startActivity(intent)
            }
            is MessageEvent.MessageCredit -> {
                Log.e("act credit", "")

                val intent = Intent()
                intent.setClassName(this@MainActivity, "com.example.credit.${event.message}")
                intent.putExtra("amount", viewModel.amount.value);
                startActivity(intent)
            }
            is MessageEvent.MessageAmount -> {
                Log.e("when amount mes", "")
                viewModel.postAmount(event.amount)
            }
            else -> {
            }
        }

    override fun onDestroy() {
        lifecycleScope.launch { viewModel.getSum()}
        super.onDestroy()
    }

}






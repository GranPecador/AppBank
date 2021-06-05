package com.example.appbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appbank.eventbus.EventBusModel
import org.example.eventbus.EventBusApp
import org.example.eventbus.MessageEvent
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    var bus: EventBusApp = EventBusApp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        textView.text = "Hey brat"

    }

    override fun onStart() {
        super.onStart()
        bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
    }


    fun onButton1(view: View) {
        bus.post(MessageEvent.MessageDeposit("DepositActivity"))
    }

    fun onButton2(view: View) {
        bus.post(MessageEvent.MessageCredit("CreditActivity"))
    }

    @Subscribe
    fun handleButton(event: MessageEvent) {
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

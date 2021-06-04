package com.example.appbank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ContextUtils.getActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    fun onButton1(view: View){
        EventBus.getDefault().post( MessageEventButton1("Hello 1!"));
    }

    fun onButton2(view: View){
        EventBus.getDefault().post( MessageEventButton2("2222222!"));
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe//(threadMode = ThreadMode.MAIN)
    fun handleButton1(event:  MessageEventButton1) {
        textView.text = event.message
        startActivity(Intent(this@MainActivity, MainActivity2::class.java))
    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    fun handleButton2(event: MessageEventButton2) {
        textView.text = event.message
    }
}


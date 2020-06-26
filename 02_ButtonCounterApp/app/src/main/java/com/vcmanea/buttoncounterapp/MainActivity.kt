package com.vcmanea.buttoncounterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

private val TAG="MainActivity"
private val TEXT_CONTETS="TextContent"
class MainActivity : AppCompatActivity() {
    var textView: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate: called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userInput: EditText = findViewById(R.id.editText)
        val button: Button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
        //Setting movement method is closely related to the the xml attribute
        textView?.movementMethod = ScrollingMovementMethod()

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                Log.d(TAG,"onClick called")
                textView?.append(userInput.text)
                textView?.append("\n")
                userInput.text.clear()

            }
        });
    }
    override fun onStart() {
        Log.d(TAG,"onStart: called")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG,"onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
        val savedString=savedInstanceState.getString(TEXT_CONTETS,"")
        textView?.text=savedString
    }

    override fun onResume() {
        Log.d(TAG,"onResume: called")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG,"onPause: called")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG,"onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_CONTETS,textView?.text.toString())
    }

    override fun onStop() {
        Log.d(TAG,"onStop: called")
        super.onStop()
    }

    override fun onRestart() {
        Log.d(TAG,"onRestart: called")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy: called")
        super.onDestroy()
    }
}

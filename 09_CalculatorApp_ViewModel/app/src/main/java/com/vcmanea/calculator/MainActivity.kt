package com.vcmanea.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    //First step consiste in removing the logic from the UI
    //Second is to subscribe MainActivity to the CalculatorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: BigDecimalViewModel by viewModels()
        viewModel.stringResult.observe(this, Observer<String> { stringResult -> resultET.setText(stringResult) })
        viewModel.stringNewNumber.observe(this, Observer<String> { stringNumber -> newNumberET.setText(stringNumber)  })
        viewModel.stringOperation.observe(this, Observer<String> { stringOperation -> operationTV.text=stringOperation  })

        val listener = View.OnClickListener {

            viewModel.digitPressed((it as Button).text.toString())
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->

            viewModel.operandPressed((v as Button).text.toString())
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            viewModel.negPressed()
        }

        Log.d(TAG, "onCreate: ends")
    }

}

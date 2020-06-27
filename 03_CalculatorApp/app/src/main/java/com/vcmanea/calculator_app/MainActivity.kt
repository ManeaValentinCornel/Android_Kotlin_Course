package com.vcmanea.calculator_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*

//cons are compile time constant.Meaning their value are assigned during compile time, unlike val which can be done at the run time
private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //************************************************LATE INIT**********************************************************************
    //The recommended way is still by using the kotlinx.synthetic import
    //LATE INIT tells kotlin nwe are using an non-nullable variable, bu will gonna defer giving it a value until later.
    //If we attept to use result before initialization the app will actually crash. It can be used only with var properties.
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    //************************************************LAZY DELEGATE*******************************************************************
    //The recommended way is still by using the kotlinx.synthetic import
    //When using by lazy you are defining a function that will be called to assign the value to the property.The function will be
    //called by to assign the value to the property.The function will be called the first time that the propery's accessed, then the valyue is cached
    //so the function won't call again
    //So as long we don;t access DisplayOperation, until onCreate has been called, this will work fine.
    // But is has the advantage that the property to be declared immutable, which can make the code more robust for the properties that won't change
    //LAZY DELEGATE IS THREAD SAFE, DISABLLE THREAD SAFETY MODE
    //It mut be val
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    //**********************************Variables to hold the operands and type of calculation****************************************
    //The reason why the first operand is null because we have to record whether it has been given a value or not
    private var operand1: Double? = null

    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b = v as Button
                newNumber.append(b.text)
            }
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

        val opListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                val op = (v as Button).text.toString()

                try {
                    val value = newNumber.text.toString().toDouble()
                    performOperation(value, op)
                } catch (e: NumberFormatException) {
                    newNumber.setText("")

                }
                pendingOperation = op
                displayOperation.text = pendingOperation
            }
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)

        buttonNeg.setOnClickListener { view->
            val value=newNumber.text.toString()
            if(value.isEmpty()){
                newNumber.setText("-")
            }
            else{
                try{
                    var doubleValue=value.toDouble()
                    doubleValue*=-1
                    newNumber.setText(doubleValue.toString())

                }
                catch (e:NumberFormatException){
                    //newNumber was "-" or ., so clear it
                    newNumber.setText("")
                }
            }
        }

        Log.d(TAG, "onCreate: ends")
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        }
        else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN// handle the attempt to divede by 0
                }
                else {
                    operand1!! / value
                }
                //BAGN BANG OPERATOR !!
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: starts")
        super.onRestoreInstanceState(savedInstanceState)

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "")
        operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        displayOperation.text = pendingOperation
        Log.d(TAG, "onRestoreInstanceState: ends")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: starts")
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }


}

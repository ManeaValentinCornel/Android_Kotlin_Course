package com.vcmanea.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

//We shouldn't expose the mutableLiveData objects, because there is a risk as the view model's owner could change them
class CalculatorViewModel : ViewModel() {

    private var operand1: Double? = null
    private var pendingOperation = "="
//*************************************************** MutableLiveData vs LiveData *********************************************************//

    //MutableLiveData
    private val newNumber = MutableLiveData<String>()
    //LiveData -> has no public method to modify its data.
    val stringNewNumber:LiveData<String>
        get()=newNumber

    private val operation = MutableLiveData<String>()
    val stringOperation:LiveData<String>
    get()=operation

    private val result = MutableLiveData<Double>()
    val stringResult: LiveData<String>
        //Transform double to sstring
    get()=Transformations.map(result,{it.toString()})


    fun digitPressed(caption: String) {
        if (newNumber.value != null) {
            newNumber.value = newNumber.value + caption
        }
        else {
            newNumber.value = caption
        }
    }

    fun operandPressed(op: String) {
        try {
            val value = newNumber.value?.toDouble()
            if (value != null) {
                performOperation(value, op)
            }
        } catch (e: NumberFormatException) {
            newNumber.value = ""
        }
        pendingOperation = op
        operation.value = pendingOperation
    }

    fun negPressed() {
        val currentValue = newNumber.value
        if (currentValue == null || currentValue.isEmpty()) {
            newNumber.value = "-"
        }
        else {
            try {
                var doubleValue = currentValue.toDouble()
                doubleValue *= -1
                newNumber.value = doubleValue.toString()

            } catch (e: NumberFormatException) {
                //newNumber was "-" or ., so clear it
                newNumber.value = ""
            }
        }
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
        result.value = operand1
        newNumber.value = ""
    }

}
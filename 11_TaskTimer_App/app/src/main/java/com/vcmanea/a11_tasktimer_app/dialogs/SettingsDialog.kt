package com.vcmanea.a11_tasktimer_app.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.vcmanea.a11_tasktimer_app.R
import kotlinx.android.synthetic.main.settings_dialog.*
import java.util.*

private const val TAG = "SettingsDialog"

const val SHARED_PREFERENCES="SharePreferencesSettings"

const val SETTINGS_FIRST_DAY_OF_WEEK = "FirstDay"

const val SETTING_IGNORE_LESS_THAN = "IgnoreLessThan"
const val SETTING_DEFAULT_IGNORE_LESS_THAN = 0


class SettingsDialog : AppCompatDialogFragment() {

    lateinit var mainActivityContext:Context
    private val defaultFirstDayOfWeek = GregorianCalendar(Locale.getDefault()).firstDayOfWeek
    private var firstDay = defaultFirstDayOfWeek
    private var ignoreLessThan= SETTING_DEFAULT_IGNORE_LESS_THAN


    override fun onAttach(context: Context) {
        mainActivityContext=context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: called")
        return inflater.inflate(R.layout.settings_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        super.onViewCreated(view, savedInstanceState)


        okButton.setOnClickListener {
            saveValues()
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()

        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewStateRestored created")
        super.onViewStateRestored(savedInstanceState)

        readValues()
        firstDaySpinner.setSelection(firstDay-GregorianCalendar.SUNDAY)//spinner values are 0
        igoneSecondsSeekBar.progress=ignoreLessThan


    }



    private fun readValues(){
        with(mainActivityContext.applicationContext.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE)){
            firstDay=getInt(SETTINGS_FIRST_DAY_OF_WEEK,defaultFirstDayOfWeek)
            ignoreLessThan=getInt(SETTING_IGNORE_LESS_THAN, SETTING_DEFAULT_IGNORE_LESS_THAN)
        }
    }



    private fun saveValues(){
        //Added one to the value, to save the time later. When it come to work with dates
        //The calendar uses 1 for Sunday, and 2 for Monday... etc
        val newFirstDay=firstDaySpinner.selectedItemPosition + GregorianCalendar.SUNDAY
        val newIgnoreLessThan=igoneSecondsSeekBar.progress

        Log.d(TAG,"Saving first day = $newFirstDay, ignore seconds= $newIgnoreLessThan")

        with(mainActivityContext.applicationContext.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE).edit()){
            if(newFirstDay!=firstDay){
                putInt(SETTINGS_FIRST_DAY_OF_WEEK,newFirstDay)
            }

            if(newIgnoreLessThan!=ignoreLessThan){
                putInt(SETTING_IGNORE_LESS_THAN,newIgnoreLessThan)
            }
            apply()
        }

    }

}
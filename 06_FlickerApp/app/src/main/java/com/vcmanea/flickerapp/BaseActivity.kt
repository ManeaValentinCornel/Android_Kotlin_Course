package com.vcmanea.flickerapp

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

internal const val FLICKER_QUERY = "FLICKER_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    private val TAG = "BaseActivity"

    internal fun activateToolbar(enableHome: Boolean) {
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        //**************************************** BASE ACTIVITY TOOLBAR***********************************************//
        //Home arrow, back-arrow which brings to the home activity
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}
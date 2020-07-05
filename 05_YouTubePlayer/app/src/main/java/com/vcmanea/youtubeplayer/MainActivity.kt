package com.vcmanea.youtubeplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPlaySingle.setOnClickListener(this)
        btnStanalone.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
     val intent=when(v?.id){
         R.id.btnPlaySingle -> Intent(this,YoutubeActivity::class.java)
         R.id.btnStanalone -> Intent(this, StandaloneActivity::class.java)
         else-> throw IllegalArgumentException("Undefined button clicked")
     }
        startActivity(intent)
    }
}

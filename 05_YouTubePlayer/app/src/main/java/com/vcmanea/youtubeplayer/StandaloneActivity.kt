package com.vcmanea.youtubeplayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.android.youtube.player.internal.s
import com.google.android.youtube.player.internal.v
import kotlinx.android.synthetic.main.activity_standalone.*
import java.lang.IllegalArgumentException

class StandaloneActivity:AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standalone)

        btnPlayVideo.setOnClickListener(this)
        btnPlaylist.setOnClickListener (this)

    }

    override fun onClick(v: View?) {
        val intent=when(v?.id){
            R.id.btnPlayVideo-> YouTubeStandalonePlayer.createVideoIntent(this,getString(R.string.GOOGLE_API_KEY), YOUTUBE_VIDEO_ID,0,true,true)
            R.id.btnPlaylist->YouTubeStandalonePlayer.createPlaylistIntent(this,getString(R.string.GOOGLE_API_KEY), YOUTUBE_PLAYLIST_ID,0,0,true,true)
            else -> throw IllegalArgumentException("Undefined button clicked")
        }

        startActivity(intent)

    }

}
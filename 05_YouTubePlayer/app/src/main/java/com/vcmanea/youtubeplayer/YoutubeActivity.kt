package com.vcmanea.youtubeplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_youtube.*

const val YOUTUBE_VIDEO_ID = "791z7Nb985Y"
const val YOUTUBE_PLAYLIST_ID = "PL94gOvpr5yt3aXGoNctRjViZPi9IlnT4P"

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val TAG="YoutubeActivity"
    private val DIALOGUE_REQUEST_CODE=1001
    val playerView by lazy { YouTubePlayerView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

//        //CREATING AND ADDING BTN TO THE LAYOUT DYNAMICALLY
//        val button1=Button(this)
//        button1.layoutParams=ConstraintLayout.LayoutParams(600,180)
//        button1.text="Button added"
//        activity_youtube.addView(button1)
        playerView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        activity_youtube.addView(playerView)
        //INITIALIZE THE OLAYER WITH THE GOOGLE API KEY
        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)

    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, youtubePlayer: YouTubePlayer?, wasRestored: Boolean) {
        Log.d(TAG,"onInitializationSuccess: provider is ${provider?.javaClass}")
        Log.d(TAG,"onInitializationSuccess: youtube player is ${youtubePlayer?.javaClass}")
        Toast.makeText(this,"Initialized Youtube player successfully", Toast.LENGTH_LONG).show()


        youtubePlayer?.setPlayerStateChangeListener(playerChangeStateListener)
        youtubePlayer?.setPlaybackEventListener(playBackEventListener)

        //WE ONLY WANT TO PLAY A NEW VIDEO IF THE PLAYER ISN'T RESUMING PLAYBACK
        if(!wasRestored){
            youtubePlayer?.loadVideo(YOUTUBE_VIDEO_ID)
        }
        else{
            //when we rotate the device
            youtubePlayer?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?, youTubeInitializationResult: YouTubeInitializationResult?) {
        val REQUEST_CODE=0
        if(youTubeInitializationResult?.isUserRecoverableError==true){
            //if the user is an recoverable error it will send a request code code to
            youTubeInitializationResult.getErrorDialog(this,DIALOGUE_REQUEST_CODE)?.show()
        }
        else{
            val errorMessage="THere was an error initializing YoutubePlayer ($youTubeInitializationResult) "
            Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show()
        }
    }

    //*******************************************PLAYBACK LISTENER**********************************************//
    private val playBackEventListener= object:YouTubePlayer.PlaybackEventListener{
        override fun onSeekTo(p0: Int) {
        }
        override fun onBuffering(p0: Boolean) {
        }
        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity,"Good, video is playing ok",Toast.LENGTH_LONG).show()
        }
        override fun onStopped() {
            Toast.makeText(this@YoutubeActivity,"Video has stopped",Toast.LENGTH_LONG).show()
        }
        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity,"Video has paused",Toast.LENGTH_LONG).show()
        }
    }

    //*******************************************CHANGE STATE LISTENER**********************************************//
    private val playerChangeStateListener=object: YouTubePlayer.PlayerStateChangeListener{
        override fun onAdStarted() {
            Toast.makeText(this@YoutubeActivity,"Click Ad now, make the video creator rich",Toast.LENGTH_LONG).show()
        }
        override fun onLoading() {
        }
        override fun onVideoStarted() {
            Toast.makeText(this@YoutubeActivity,"Video started",Toast.LENGTH_LONG).show()
        }
        override fun onLoaded(p0: String?) {
        }
        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity,"Congratulations! You've completed another video",Toast.LENGTH_LONG).show()
        }
        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG,"onActivityResult called with responde  $resultCode fore request $requestCode")

        if(requestCode==DIALOGUE_REQUEST_CODE){
            Log.d(TAG,intent?.toString())
            Log.d(TAG,intent?.extras.toString())
            playerView.initialize(getString(R.string.GOOGLE_API_KEY),this)
        }

    }


}

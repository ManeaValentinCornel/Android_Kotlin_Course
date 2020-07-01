package com.vcmanea.top10_downloaderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

private const val FEED_URL="feed_url"
private const val FEED_LIMIT="feed_limit"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    //%d is a way of specifying a integer value which will be replaced by an actual value, by using string.format()
    private var feedUrl: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"//<-- %d is here
    private var feedLimit:Int = 10
    //Problem here is that the pass the reference to our listView, and the list won;t exist until the setContentView of the onCreate method
    //The solution is to use by lazy so the initialization is not performed until first time we need to use our download data object
    //By the time we used our downloadData object, the setContentView method it will be already called
    //IT HAS TO BE INITIALIZE AS NULL, the same instace cannot be used multiple times -> because async task can execute only once
    //and that will result in not being able to change from the menu, the feed topic
    //private val downloadData by lazy { DownloadDataAsync(this, xmlListView) }
    private var downloadData: DownloadDataAsync? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")

        //Checking to see if the saved instace state was executed previsuly, adn if it was change the values of the feed url and feed limit with the prvios ones
     if(savedInstanceState!=null){
             feedUrl=savedInstanceState.getString(FEED_URL,"")
             feedLimit=savedInstanceState.getInt(FEED_LIMIT)

     }






        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //When the extecute method is called it takes care of setting up all the multi-threading nad then runs doingInBackground thread,
        //Once the task completes the asyng task gets the return value from the other thread(doInBackground) and call the onPostExecute
        downloadURL(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate done")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FEED_URL,feedUrl)
        outState.putInt(FEED_LIMIT,feedLimit)
        Log.d(TAG,"onSaveInstanceState called")
    }


    override fun onDestroy() {
        super.onDestroy()
        //VERY IMPORTANT -> canceling the asynktask
        downloadData?.cancel(true)
    }
    //************************************************************MENU******************************************************************//

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if(feedLimit==10){
            menu?.findItem(R.id.mnu10)?.isChecked=true
        }
        else{
            menu?.findItem(R.id.mnu25)?.isChecked=true
        }

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val previousValue=feedUrl
        val previousLimit=feedLimit
        when (item.itemId) {
            R.id.mnuFree -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml" //<-- %d is here
            R.id.mnuPaid -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"//<-- %d is here
            R.id.mnuSongs -> feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"//<-- %d is here

            R.id.mnu10, R.id.mnu25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    //A very neat way to toggle between 10 and 25
                    feedLimit = 35 - feedLimit
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feedLimit to $feedLimit")
                }
                else {
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feedLimit to unchanged")
                }
            }
            R.id.mnuRefresh->{ downloadURL(feedUrl.format(feedLimit))
                return true}
            //The else branch is very important, and it should always be included when creating code to react to menu choices
            //When it comes to the submenus, when you are going in one of the submenus, android triggers a call to this method when the submenu is opened
            //if you don't call this method any code after the when branch will execute.... in this example the downloadURl() with an empty String, which is unacceptable

            else -> return super.onOptionsItemSelected(item)
        }


        //DECIDING IF THE REQUESTED TEMP FEED IS THE ACTUAL FEED,AND IF IT'S DON'T DO ANYTHING, AND ALSO TO SEE IF THE LIMIT WAS CHANGE
        if(previousValue==feedUrl && previousLimit==feedLimit){
           Log.d(TAG,"onOptionsItemSelected SAME")
            return super.onOptionsItemSelected(item)
        }

        downloadURL(feedUrl.format(feedLimit))
        return true
    }

    private fun downloadURL(feedUrl: String) {
        //When the extecute method is called it takes care of setting up all the multi-threading nad then runs doingInBackground thread,
        //Once the task completes the asyng task gets the return value from the other thread(doInBackground) and call the onPostExecute
        Log.d(TAG, "downloadURL starting AsyncTask")
        //ASYNCTASK CANNOT BE USED MORE THAN ONCE
        downloadData = DownloadDataAsync(this, xmlListView)
        downloadData?.execute(feedUrl)
        Log.d(TAG, "downloadURL done")
    }

}
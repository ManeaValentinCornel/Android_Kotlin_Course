package com.vcmanea.flickerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONException

private const val TAG = "MainActivity"

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {
    private val flickRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //**************************************** BASE ACTIVITY TOOLBAR***********************************************//
        activateToolbar(false)


        //****************************************RECYCLER  VIEW***********************************************//
        //The recycler view doesn't take care of handling the layouts, that's done by the layout manager
        //-> notifyDataSetChange is in onDataAvailable callback
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recycler_view, this))
        recycler_view.adapter = flickRecyclerViewAdapter

    }
    //**************************************** ON RESUME- > SHARED PREFERENCE***********************************************//
    override fun onResume() {
        Log.d(TAG, "onResume: starts")
        val sharedPref=applicationContext.getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE)
        val queryResult=sharedPref.getString(getString(R.string.shared_flicker_query),"")
        if(queryResult?.isNotEmpty() ?:false){

            //**************************************** URI BUILDER***********************************************//
            val url = FlickerUriBuilder.createUri("https://www.flickr.com/services/feeds/photos_public.gne", queryResult.toString(), "en-us", true)
            val getRawData = GetRawData(this)

            //**************************************** ASYNC ***********************************************//
            getRawData.execute(url)
        }
        super.onResume()
    }



    //**************************************** TOOLBAR MENU **********************************************//
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.app_bar_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //**************************************** DOWNLOAD JSON **********************************************//
    override fun onDownloadComplete(data: String?, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadCoplete called, data is ready to be parsed")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }
        else {
            Log.d(TAG, "onDownloadCompleted failed with status $status. Error mesage is $data")
        }
    }

    //**************************************** PARSE JSON **********************************************//
    override fun onDataAvailable(list: ArrayList<Photo>?) {
        Log.d(TAG, "onDataAvailable called")
        //-> notify when the list was updated
        list?.let { flickRecyclerViewAdapter.loadNewData(list) }
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(error: JSONException) {
        Log.d(TAG, "onDataAvailable called, data with exception ${error.message}")
    }
    //**************************************** RECYCLER VIEW CLICK LISTENER **********************************************//

    override fun onItemClick(view: View?, position: Int?) {
        Log.d(TAG, "onItemClick called")
        position?.let { launchPhotoDetails(it) }
        Toast.makeText(this, "Short tap at position, $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View?, position: Int?) {
        Log.d(TAG, "onItemLongClick called")
        Toast.makeText(this, "Long tap at position, $position", Toast.LENGTH_SHORT).show()
    }

    //**************************************** LAUNCH PhotoDetails activity **********************************************//
    private fun launchPhotoDetails(position: Int) {
        Log.d(TAG, "onItemLongClick called")
        val photo = flickRecyclerViewAdapter.getPhotoDetails(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }
}

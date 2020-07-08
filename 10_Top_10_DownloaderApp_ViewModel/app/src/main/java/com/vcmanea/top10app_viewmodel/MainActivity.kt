package com.vcmanea.top10app_viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


private const val FEED_URL = "feed_url"
private const val FEED_LIMIT = "feed_limit"

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //%d is a way of specifying a integer value which will be replaced by an actual value, by using string.format()
    private var feedUrl: String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"//<-- %d is here
    private var feedLimit: Int = 10

    val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val feedAdapter = FeedAdapter(this, R.layout.list_record, Collections.emptyList())
        xmlListView.adapter = feedAdapter

        viewModel.feedEntries.observe(this, Observer<List<FeedEntry>> { feedEntries -> feedAdapter.setFeedList(feedEntries) })

        //Checking to see if the saved instance state was executed previously, adn if it was change the values of the feed url and feed limit with the previous ones
        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(FEED_URL, "")
            feedLimit = savedInstanceState.getInt(FEED_LIMIT)

        }

        viewModel.downloadURL(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate done")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FEED_URL, feedUrl)
        outState.putInt(FEED_LIMIT, feedLimit)
        Log.d(TAG, "onSaveInstanceState called")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)
        if (feedLimit == 10) {
            menu?.findItem(R.id.mnu10)?.isChecked = true
        }
        else {
            menu?.findItem(R.id.mnu25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val previousValue = feedUrl
        val previousLimit = feedLimit
        when (item.itemId) {
            R.id.mnuFree -> {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            }
            R.id.mnuPaid -> {
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show()
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            }

            R.id.mnuSongs -> {
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            }

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
            R.id.mnuRefresh -> {
                viewModel.downloadURL(feedUrl.format(feedLimit))
                return true
            }
            //The else branch is very important, and it should always be included when creating code to react to menu choices
            //When it comes to the submenus, when you are going in one of the submenus, android triggers a call to this method when the submenu is opened
            //if you don't call this method any code after the when branch will execute.... in this example the downloadURl() with an empty String, which is unacceptable

            else -> return super.onOptionsItemSelected(item)
        }


        //DECIDING IF THE REQUESTED TEMP FEED IS THE ACTUAL FEED,AND IF IT'S DON'T DO ANYTHING, AND ALSO TO SEE IF THE LIMIT WAS CHANGE
        if (previousValue == feedUrl && previousLimit == feedLimit) {
            Log.d(TAG, "onOptionsItemSelected SAME")
            return super.onOptionsItemSelected(item)
        }

        viewModel.downloadURL(feedUrl.format(feedLimit))
        return true
    }


}
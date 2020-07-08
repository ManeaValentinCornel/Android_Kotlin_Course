package com.vcmanea.top10app_viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG ="FeedViewModel"
class FeedViewModel: ViewModel(),DownloadDataAsync.DownloaderCallBack{
    private var downloadData: DownloadDataAsync? = null

    private val feed=MutableLiveData<List<FeedEntry>>()
    val feedEntries:LiveData<List<FeedEntry>>
    get()=feed

    init{
        //in order to avoid any errors, unitl we initialize the live data object with somethign they will return null
        //just to make sure we won't get any errors in result of returning null, ny initializing the MutableLiveData object straight away
        //epty list which Collection provide for us, immutable
        feed.postValue(Collections.emptyList())
    }

     fun downloadURL(feedUrl: String) {
        downloadData = DownloadDataAsync(this)
        downloadData?.execute(feedUrl)
        Log.d(TAG, "downloadURL done")
    }


    override fun onCleared() {
    Log.d(TAG,"onCleared: cancelling pending download")
    downloadData?.cancel(true)
    }


    override fun onDowloadComplete(result: List<FeedEntry>) {
        feed.value=result
    }
}
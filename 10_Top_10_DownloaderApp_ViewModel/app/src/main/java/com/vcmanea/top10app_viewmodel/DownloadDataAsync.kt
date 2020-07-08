package com.vcmanea.top10app_viewmodel;

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

// ***************************************ASYNC TASK TO DOWNLOAD RAW DATA *********************************************************************/
//THe difference between an inner class and a static nested class is that the inner class hold a reference to the activity
//A static nested class doesn't, in fac exists idepentendly of the class tat it's nested in, it is pretty much like a package convenience with that
//particular return value
private val TAG = "DownloadData"

class DownloadDataAsync(private val callBack: DownloaderCallBack) :
    AsyncTask<String, Void, String>() {
    //Interface
    interface DownloaderCallBack {
        fun onDowloadComplete(result: List<FeedEntry>)
    }

    //execute on a background thread
    override fun doInBackground(vararg urls: String?): String {
        Log.d(TAG, "doInBackGround: starts with ${urls[0]}")

        val rssFeed: String = downloadXML(urls[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloadding")
        }
        return rssFeed
    }

    //OnPostExecute is called on the main thread, the porameter passed to onPost execute is the return value of the doInBackground
    override fun onPostExecute(result: String) {
        val xmlParser=ParseXMLApplications()
        if (result.isNotEmpty()) {
            xmlParser.parse(result)
        }
        callBack.onDowloadComplete(xmlParser.applicationsList)
    }

    // ************************************************** DOWNLOAD XML METHOD *********************************************************************/
    private fun downloadXML(urlPath: String?): String {
        try {
            return URL(urlPath).readText()
        } catch (e: Exception) {
            val errorMessage: String = when (e) {
                is MalformedURLException -> "downloadXml: Invalid URL ${e.message}"
                is IOException -> "downloadXML: IO Exception ${e.message}"
                is SecurityException-> {
                    e.printStackTrace()
                    "downloadXML: Security exception. Need permison? ${e.message}"
                }
                else -> "downloadXML: Unknown error ${e.message}"
            }

        }
        return ""
    }
}
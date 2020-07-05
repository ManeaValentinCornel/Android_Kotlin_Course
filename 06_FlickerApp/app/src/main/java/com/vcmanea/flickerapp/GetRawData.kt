package com.vcmanea.flickerapp

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}
class GetRawData(private val listener:OnDownloadComplete) : AsyncTask<String, Void, String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    //************************************ CALLBACK INTERFACE ******************************************************//
    interface OnDownloadComplete{
        fun onDownloadComplete(data:String?,status:DownloadStatus)
    }
    //************************************DOWNLOAD DATA ASYNC******************************************************//
    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG,"doInBackground called")
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }
        catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException-> {
                    downloadStatus=DownloadStatus.FAILED_OR_EMPTY
                    e.printStackTrace()
                    "doInBackground: IO Exception reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus=DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security exception: Needs permission ${e.message}"
                }
                else -> {
                    downloadStatus=DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG,errorMessage)
            return errorMessage

        }
    }
    override fun onPostExecute(result: String?) {
        Log.d(TAG,"onPostExecute called")
        listener.onDownloadComplete(result,downloadStatus)
    }


}
package com.vcmanea.flickerapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class GetFlickrJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<Photo>>() {
    private val TAG = "GetFlickrJsonData"

    //************************************ CALLBACK INTERFACE ******************************************************//
    interface OnDataAvailable {
        fun onDataAvailable(list: ArrayList<Photo>?)
        //Show how to handle exception when they happend on another thread
        //Another way to handle-> have a look on GetRawData
        fun onError(error: JSONException)
    }
    //************************************PARSING JSON ASYNC******************************************************//
    private val JSON_ITEMS_ARRAY = "items"
    private val JSON_TITLE = "title"
    private val JSON_AUTHOR = "author"
    private val JSON_AUTHOR_ID = "author_id"
    private val JSON_TAGS = "tags"
    private val JSON_MEDIA = "media"
    private val JSON_IMAGE_URL = "m"
    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(TAG, "doInBackground starts")
        val photoList = ArrayList<Photo>()

        try {
            val jsonData = JSONObject(params[0])
            //1 Get the array of items
            val itemsArray = jsonData.getJSONArray(JSON_ITEMS_ARRAY)
            for (i in 0 until itemsArray.length()) {
                //2 Get each individual object
                val jsonObject = itemsArray.getJSONObject(i)
                //3 Get each object properties
                val title = jsonObject.getString(JSON_TITLE)
                val author = jsonObject.getString(JSON_AUTHOR)
                val authodId = jsonObject.getString(JSON_AUTHOR_ID)
                val tags = jsonObject.getString(JSON_TAGS)
                //IMPORTANT -> each "jsonMedia" object has a url property "m"
                val media = jsonObject.getJSONObject(JSON_MEDIA)
                val imageUrlSmall = media.getString(JSON_IMAGE_URL)
                val imageUrlBig = imageUrlSmall.replace("_m.jpg", "_b.jpg")

                val photoObject = Photo(title, author, authodId, tags, imageUrlSmall, imageUrlBig)
                photoList.add(photoObject)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing Json data. ${e.message}")
            //VERY VERY VERY IMPORTANT, IF THERE IS AN ERROR, WE HAVE TO CANCEL THE THREAD TO PREVENT THE ONPOSTEXECUTE TOO BE CALLED WITH A NULL PARAMETER
            cancel(true)
            listener.onError(e)
        }
        Log.d(TAG, "doInBackground ends")
        return photoList
    }
    override fun onPostExecute(result: ArrayList<Photo>?) {
        Log.d(TAG, "onPostExecute starts")
        listener.onDataAvailable(result)
        Log.d(TAG, "onPostExecute ends")
    }
}
package com.vcmanea.top10_downloaderapp

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import java.net.URL

// ***************************************ASYNC TASK TO DOWNLOAD RAW DATA *********************************************************************/
//THe difference between an inner class and a static nested class is that the inner class hold a reference to the activity
//A static nested class doesn't, in fac exists idepentendly of the class tat it's nested in, it is pretty much like a package convenience with that
//particular return value
class DownloadDataAsync(var context: Context, var listView : ListView) : AsyncTask<String, Void, String>() {
    private val TAG = "DownloadData"

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
        super.onPostExecute(result)
        val parseXMLApplications = ParseXMLApplications()
        parseXMLApplications.parse(result)
        val arrayAdapter = FeedAdapter(context, R.layout.list_record, parseXMLApplications.applicationsList)
        listView.adapter = arrayAdapter
    }

    // ************************************************** DOWNLOAD XML METHOD *********************************************************************/
    private fun downloadXML(urlPath: String?): String {
    // ****************************************************** LONG WAY *********************************************************************/
//            val xmlResult = StringBuilder()
//            try {
//                val url = URL(urlPath)
//                val connection = url.openConnection() as HttpURLConnection
//                val response = connection.responseCode
//                Log.d(TAG, "downloadXML: The response code was $response")
//                //LONG WAY
////                val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
////                val inputBuffer = CharArray(500)
////                var charRead = 0
////                while (charRead >= 0) {
////                    charRead = bufferedReader.read(inputBuffer)
////                    if (charRead > 0) {
////                        xmlResult.append(String(inputBuffer, 0, charRead))
////                    }
////                }
////                bufferedReader.close()
////                IDIOMATIC WAY
//                connection.inputStream.buffered().reader().use {
//                    xmlResult.append(it.readText())
//                }
//                Log.d(TAG, "Received ${xmlResult.length} bytes")
//                return xmlResult.toString()
//            }
////            catch(e : MalformedURLException){
////                Log.e(TAG,"downloadXML: Invalid URL ${e.message}")
////            }
////            catch(e : IOException){
////                Log.e(TAG,"downloadXML: IO Exception reading data ${e.message}")
////            }
////            catch(e: SecurityException){
////                e.printStackTrace()
////                Log.e(TAG,"downloadXML: Security exception. Need permission ? ${e.message}")
////            }
////            catch(e : Exception){
////                Log.e(TAG,"downloadXML: Unknown errpr ${e.message}")
////            }
        //            //*********************************************************IDIOMATIC WAY*********************************************************//
//            catch (e: Exception) {
//                val errorMessage: String = when (e) {
//                    is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
//                    is IOException -> "downloadXML: IO Exception reading data ${e.message}"
//                    is SecurityException -> {
//                        e.printStackTrace()
//                        "downloadXML: Security exception. Need permission ? ${e.message}"
//                    }
//                    else -> "downloadXML: Unknown errpr ${e.message}"
//                }
//            }
//            return ""// If it gets to here, there's been a problem. Return an empty string
// ****************************************************** KOTLIN IDIOMATIC WAY *********************************************************************/
        //FOR VERY BIG DATA THIS METHOD SHOULD BE USED, READ MORE ON ANDROID(KOTLIN DOC)
        return URL(urlPath).readText()
    }
}
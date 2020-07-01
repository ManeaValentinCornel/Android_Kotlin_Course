package com.vcmanea.top10_downloaderapp
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ParseXMLApplications {
    private val XML_TITLE="title"
    private val XML_ARTIST="im:artist"
    private val XML_UPDATED="updated"
    private val XML_SUMMARY="summary"

    private val TAG = "ParseApplications"
    val applicationsList = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
//        Log.d(TAG, "parse method called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //VERY IMPORTTANT TO USE HERE SAFE CALL OPERATOR ?., because the tag might be null
                val tagName = xpp.name?.toLowerCase(Locale.ENGLISH)
                when (eventType) {
                    //1 ENTERING INSIDE AN ENTRY -> <entry>
                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG, "parse: Starting tag for $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }
                    //2 GETTING THE TEXT BETWEEN TAGS
                    XmlPullParser.TEXT -> textValue = xpp.text
                    //3 IF WE ARE INSIDE ANY ENTRY TAG, FOR EXAMPLE <entry> <name> text here </name> </entry> and our END_TAG matches </name> save the data into name FeedEntr name property
                    XmlPullParser.END_TAG -> {
//                        Log.d(TAG, "parse Ending tag for $tagName ")
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applicationsList.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry() //create new object
                                }
                                XML_TITLE -> currentRecord.title = textValue
                                XML_ARTIST -> currentRecord.artist = textValue
                                XML_UPDATED -> currentRecord.updated = textValue
                                XML_SUMMARY -> currentRecord.summary = textValue
                            }
                        }
                    }
                }
                //Look for the next event, parse further through xml
                eventType = xpp.next()
            }
            //JUST FOR LOGGING
//            for(app in applicationsList){
//                Log.d(TAG,"******************************")
//                Log.d(TAG,app.toString())
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }
}
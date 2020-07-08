package com.vcmanea.top10app_viewmodel;
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
                val tagName = xpp.name?.toLowerCase(Locale.ENGLISH)
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> {

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
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
        return status
    }
}
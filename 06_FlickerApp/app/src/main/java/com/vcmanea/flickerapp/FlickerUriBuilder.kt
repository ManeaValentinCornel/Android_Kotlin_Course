package com.vcmanea.flickerapp

import android.net.Uri

object FlickerUriBuilder {


    fun createUri(baseUrl: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        //****************************************DETAILED WAY ***********************************************//
//        var uri = Uri.parse(baseUrl)
//        var builder = uri.buildUpon()
//        builder = builder.appendQueryParameter("tags", searchCriteria)
//        builder = builder.appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
//        builder = builder.appendQueryParameter("lang", lang)
//        builder = builder.appendQueryParameter("format", "json")
//        builder = builder.appendQueryParameter("nojsoncallback", "1")
//        uri=builder.build()
//
//        return uri.toString()
        //****************************************Neat Way ***********************************************//
        var uri = Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()

        return uri.toString()
    }


}

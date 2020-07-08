package com.vcmanea.top10app_viewmodel;

open class FeedEntry(var title:String="",var artist:String="",var updated:String="",var summary:String="",var imageURL:String="") {


    override fun toString(): String {
        return """
            |title=$title
            |artist=$artist
            |updated=$updated
            |summary=$summary
            |imageURL=$imageURL
        """.trimMargin("|")
    }
}
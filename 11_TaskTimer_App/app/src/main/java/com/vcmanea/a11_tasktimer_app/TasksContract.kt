package com.vcmanea.a11_tasktimer_app

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns


object TasksContract {
    internal const val TABLE_NAME = "Tasks"
    //Here we're creating a new URI to access the Tasks table, will be used by external classes, including different
    //apps that user our content provider to refer to our Tasks table
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    //MIME TYPES
    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    //Tasks fields
    object Columns {
        const val ID = BaseColumns._ID
        const val TASK_NAME = "Name"
        const val TASK_DESCRIPTION = "Description"
        const val TASK_SORT_ORDER = "SortOrder"
    }

    fun getIdFromUri(uri: Uri): Long {
        //Used to extract the id from the uri   - > content://authority/path/id    ->if the path is empty the result will be -1
        return ContentUris.parseId(uri)
    }

    //Used when you want to queryy just a row, in main activity more details
    fun buildUriFromId(id: Long): Uri {
        //Create the Uri from the id
        //we could use the method withAppendedPath which we used previously for CONTENT_URI -> by then we would have to convert the ID into a String
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

}

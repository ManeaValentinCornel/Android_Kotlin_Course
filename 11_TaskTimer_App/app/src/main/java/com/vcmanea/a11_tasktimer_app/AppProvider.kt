package com.vcmanea.a11_tasktimer_app

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log
import java.lang.IllegalArgumentException

/**
 * Provider for the TaskTimer app. This is the only class that knows about [AppDatabase]
 */
private const val TAG = "AppProvider"
const val CONTENT_AUTHORITY = "com.vcmanea.a11_tasktimer_app.provider"

private const val TASKS = 100
private const val TASKS_ID = 101

private const val TIMINGS = 200;
private const val TIMINGS_ID = 201

private const val TASK_DURATIONS = 400
private const val TASK_DURATIONS_ID = 401

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class AppProvider : ContentProvider() {
    private val uriMathcer by lazy { buildUriMatcher() }

    //*************************************************URI MATCHER *************************************************************//

    //-> URI MATCHER maps different content URIs to integer values, which we can decide after which code to execute for various URIs
    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        //-1 is the value of UriMatcher.NO_MATCH
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        //e.g. content://com.vcmanea.a11_tasktimer_app.provider.Tasks
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS)
        //e.g. content://com.vcmanea.a11_tasktimer_app.provider.Tasks/2  -> Matches a content URI for single row in table as 2 in our example
        matcher.addURI(CONTENT_AUTHORITY, "${TasksContract.TABLE_NAME}/#", TASKS_ID)

        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS)
        matcher.addURI(CONTENT_AUTHORITY, "${TimingsContract.TABLE_NAME}/#", TIMINGS_ID)

//        matcher.addURI(CONTENT_AUTHORITY,DutationContract.TABLE_NAME,TASK_DURATIONS)
//        matcher.addURI(CONTENT_AUTHORITY,"${DutationContract.TABLE_NAME}/#", TASK_DURATIONS_ID)

        return matcher
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {

        Log.d(TAG, "query: called with the uri $uri")
        //Getting the integer value from the uri // content://authority/path/id
        val match = uriMathcer.match(uri)
        Log.d(TAG, "query: match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when (match) {

            TASKS -> queryBuilder.tables = TasksContract.TABLE_NAME //-> will be mapped to 100

            TASKS_ID -> {
                queryBuilder.tables = TasksContract.TABLE_NAME //-> will get mapped to 101
                val taskId = TasksContract.getIdFromUri(uri)

                /************************************APPEND WHERE VS APPENDS WHERE ESCAPE STRING  *****************************************/

                //queryBuilder.appendWhereEscapeString("${TasksContract.Columns.ID}=$taskId") --> WRONG because where 'id=20' instead of where id='20'
                //Against SQL INJECTION ATACKS, append
                //Appende the values not the entire where close
                queryBuilder.appendWhere("${TasksContract.Columns.ID}=")
                queryBuilder.appendWhereEscapeString("$taskId")
            }

            TIMINGS -> queryBuilder.tables = TimingsContract.TABLE_NAME //-> will be mapped to 200

            TIMINGS_ID -> {
                queryBuilder.tables = TimingsContract.TABLE_NAME //-> will get mapped to 201
                val taskId = TimingsContract.getIdFromUri(uri)
                queryBuilder.appendWhere("${TimingsContract.Columns.ID}=")
                queryBuilder.appendWhereEscapeString("$taskId")

            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")

        }
        val db = AppDatabase.getInstance(context!!).readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        Log.d(TAG, "query: rows in returned cursor ${cursor.count}")

        return cursor

    }

    //*************************************************MIME TYPE *************************************************************//
    //-> Basically MIME TYPES are to let other system know what kind of data they expect to get back from our ContentProvider
    //-> A program may request a mine type of our URIs and use it to work out to do with the dat return
    //-> However this app won't use this types
    override fun getType(uri: Uri): String? {

        val match = uriMathcer.match(uri)

        return when (match) {
            TASKS -> TasksContract.CONTENT_TYPE
            TASKS_ID -> TasksContract.CONTENT_TYPE

            TIMINGS -> TasksContract.CONTENT_TYPE
            TIMINGS_ID -> TasksContract.CONTENT_TYPE

            TASK_DURATIONS -> TasksContract.CONTENT_TYPE
            TASK_DURATIONS_ID -> TasksContract.CONTENT_TYPE

            else -> throw  IllegalArgumentException("unknow URi: $uri")
        }
    }

    override fun onCreate(): Boolean {
        //Because the database is implemented as a singleton we cannot create the database here, but he have to return true instead
        return true
    }

    //The function accepts an URI without an id, and return the same URI with id
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert: called with the uri $uri")
        //Getting the integer value from the uri // content://authority/path/id
        val match = uriMathcer.match(uri)
        Log.d(TAG, "insert: match is $match")
        val recordId: Long
        val returnUri: Uri

        when (match) {
            //Getting readable and writable database can be quite slow, so we want to avoid calling them if an invalid URI's used
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(TasksContract.TABLE_NAME, null, values)
                //WIll return -1 if the insert failed
                if (recordId != -1L) {
                    returnUri = TasksContract.buildUriFromId(recordId)
                }
                else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            TIMINGS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(TimingsContract.TABLE_NAME, null, values)

                if (recordId != -1L) {
                    returnUri = TimingsContract.buildUriFromId(recordId)
                }
                else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            else -> throw IllegalArgumentException("Unknown uri: $uri")

        }
        //When we call a function of the ContentProvider that result in a change to the database, the ContentResolver will notify any observes about the change
        //The second part is to observe this notification, in TaskTimerViewModel
        if(recordId>0){
            //something was inserted
            Log.d(TAG, "insert: Setting notifyChange with $uri")
            context?.contentResolver?.notifyChange(uri,null)
        }
        Log.d(TAG, "Exiting insert, returning $returnUri")
        return returnUri

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "update: called with the uri $uri")
        //Getting the integer value from the uri // content://authority/path/id
        val match = uriMathcer.match(uri)
        Log.d(TAG, "update: match is $match")

        val count: Int
        var selectionCriteria: String

        when (match) {
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(TasksContract.TABLE_NAME, values, selection, selectionArgs)

            }
            //Matches a content URI for single row in table
            TASKS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TasksContract.getIdFromUri(uri)

                //selection criteria by default here will be the row number which we select to update
                selectionCriteria = "${TasksContract.Columns.ID} = $id"
                //in case when we call the insert method we want to add another where clause
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(TasksContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            TIMINGS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs)
            }
            //Matches a content URI for single row in table
            TIMINGS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TimingsContract.getIdFromUri(uri)

                //selection criteria by default here will be the row number which we select to update
                selectionCriteria = "${TimingsContract.Columns.ID} = $id"
                //in case when we call the insert method we want to add another where clause
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unknw uri $uri")
        }

        //When we call a function of the ContentProvider that result in a change to the database, the ContentResolver will notify any observes about the change
        //The second part is to observe this notification, in TaskTimerViewModel
        if(count>0){
            //something was inserted
            Log.d(TAG, "update: Setting notifyChange with $uri")
            context?.contentResolver?.notifyChange(uri,null)
        }

        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete: called with the uri $uri")
        //Getting the integer value from the uri // content://authority/path/id
        val match = uriMathcer.match(uri)
        Log.d(TAG, "delete: match is $match")

        val count: Int
        var selectionCriteria: String

        when (match) {
            TASKS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.delete(TasksContract.TABLE_NAME, selection, selectionArgs)

            }
            //Matches a content URI for single row in table
            TASKS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TasksContract.getIdFromUri(uri)

                //selection criteria by default here will be the row number which we select to update
                selectionCriteria = "${TasksContract.Columns.ID} = $id"
                //in case when we call the insert method we want to add another where clause
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(TasksContract.TABLE_NAME, selectionCriteria, selectionArgs)
            }

            TIMINGS -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.delete(TimingsContract.TABLE_NAME, selection, selectionArgs)

            }
            //Matches a content URI for single row in table
            TIMINGS_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = TimingsContract.getIdFromUri(uri)
                //selection criteria by default here will be the row number which we select to update
                selectionCriteria = "${TimingsContract.Columns.ID} = $id"
                //in case when we call the insert method we want to add another where clause
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs)

            }

            else -> throw IllegalArgumentException("Unknw uri $uri")

        }
        Log.d(TAG, "Exiting delete, returning $count")
        //When we call a function of the ContentProvider that result in a change to the database, the ContentResolver will notify any observes about the change
        //The second part is to observe this notification, in TaskTimerViewModel
        if(count>0){
            //something was inserted
            Log.d(TAG, "delete: Setting notifyChange with $uri")
            context?.contentResolver?.notifyChange(uri,null)
        }
        return count
    }


}
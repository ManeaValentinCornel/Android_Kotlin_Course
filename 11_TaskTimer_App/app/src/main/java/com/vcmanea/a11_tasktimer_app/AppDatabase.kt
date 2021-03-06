package com.vcmanea.a11_tasktimer_app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.nfc.Tag
import android.util.Log
import java.lang.IllegalStateException

/**
 * Basic database class for the application
 *
 * The only class that should use this is [AppProvider]
 */
private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "TaskTimer.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        //CREATE TABLE Tasks (_id INTEGER PRIMARY KEY NULL, Name TEXT NOT NULL, Description TEXT, SortOrder INTEGER);
        Log.d(TAG, "onCreate: starts")
        val sSQL = """CREATE TABLE ${TasksContract.TABLE_NAME} (
            ${TasksContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${TasksContract.Columns.TASK_NAME} TEXT NOT NULL,
            ${TasksContract.Columns.TASK_DESCRIPTION} TEXT,
            ${TasksContract.Columns.TASK_SORT_ORDER} INTEGER);
        """.replaceIndent(" ")
        Log.d(TAG, sSQL)
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: starts")
        when (oldVersion) {
            1 -> {
                //upgrade logic from version 1
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVesrion $newVersion")
        }

    }

    //Can be used in any class taht has a single argument constructor
    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)

    //TIM WAY OF SINGLETON
//    companion object {
////        @Volatile
////        private var instance: AppDatabase? = null
////        fun getInstance(context: Context): AppDatabase=
////            instance ?: synchronized(this) {
////                instance ?: AppDatabase(context).also { instance = it }
////            }
////    }
}
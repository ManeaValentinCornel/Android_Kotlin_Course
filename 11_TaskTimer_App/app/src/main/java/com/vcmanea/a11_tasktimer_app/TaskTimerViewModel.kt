package com.vcmanea.a11_tasktimer_app

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

//AndroidVieModel is a subclass of ViewModel but is also has an application property, that is gonna be useful if we want to access the database
//because we'll need an application to get the Content resolver
private const val TAG = "TaskTimerViewHolder"

class TaskTimerViewModel(application: Application) : AndroidViewModel(application) {


    //Receives call backs for changes to content, observes the notification recievied from the ContentProvider(ProviderClass)
    private val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            Log.d(TAG, "contentObserver.onChange : called. uri is $uri")
            loadTasks()
        }
    }

    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
        get() = databaseCursor

    init {
        Log.d(TAG, "created")
        //Register the content resolver, which will react when a notification is received
        getApplication<Application>().contentResolver.registerContentObserver(TasksContract.CONTENT_URI, true, contentObserver)
        loadTasks()
    }

    private fun loadTasks() {
        //Standard code for fetching data from the content provider
        val projection = arrayOf(
            TasksContract.Columns.ID,
            TasksContract.Columns.TASK_NAME,
            TasksContract.Columns.TASK_DESCRIPTION,
            TasksContract.Columns.TASK_SORT_ORDER
        )
        //<order by> Tasks.SortOrder, Tasks.Name
        val sortOrder = "${TasksContract.Columns.TASK_SORT_ORDER} , ${TasksContract.Columns.TASK_NAME}"

        //COUROUTINE
        GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver.query(
                TasksContract.CONTENT_URI,
                projection, null, null,
                sortOrder
            )
            databaseCursor.postValue(cursor)
        }
    }


    fun saveTask(task: Task): Task {
        val values = ContentValues()
        if (task.name.isNotEmpty()) {
            //Don't save the task with no name
            values.put(TasksContract.Columns.TASK_NAME, task.name)
            values.put(TasksContract.Columns.TASK_DESCRIPTION, task.description)
            values.put(TasksContract.Columns.TASK_SORT_ORDER, task.sortOrder)   //defaults to zero if emtpy

            //ADD
            if (task.id == 0L) {
                //if task =0 means we have a new task
                GlobalScope.launch {
                    Log.d(TAG, "saveTask: adding new task")
                    val uri = getApplication<Application>().contentResolver?.insert(TasksContract.CONTENT_URI, values)
                    if (uri != null) {
                        task.id = TasksContract.getIdFromUri(uri)
                        Log.d(TAG, "saveTask: new id is ${task.id}")
                    }
                }
            }
            //EDIT
            else{
                //task has an id, so we're updating
                GlobalScope.launch {
                   Log.d(TAG,"saveTask: updating task")
                    getApplication<Application>().contentResolver?.update(TasksContract.buildUriFromId(task.id),values,null,null)
                }
            }
        }
        //the reason of returning the task if now we may have an id which we didn't have before
        return task
    }


    fun deleteTask(taskId: Long) {
        //DELETE ON A BACKGROUND THREAD
        thread {
            Log.d(TAG, "Deleting task")
            getApplication<Application>().contentResolver.delete(TasksContract.buildUriFromId(taskId), null, null)
        }

    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: called")
        //Unregister the cotnent resolver
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }


    //KOTLIN COROUTINE
    //Kotlin coroutines provide similar functionality to thread, but are more efficent. The reason is that creating a new thread takes time
    //it's quite expensive operation to perform

}
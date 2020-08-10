package com.vcmanea.a11_tasktimer_app.fragments.addEditFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vcmanea.a11_tasktimer_app.R
import com.vcmanea.a11_tasktimer_app.Task
import com.vcmanea.a11_tasktimer_app.TaskTimerViewModel
import kotlinx.android.synthetic.main.fragment_add_edit.*

//Argumets that we're gonna pass to the fragment in a bundle
private const val TAG = "AddEditFragment"
private const val ARG_TASK = "task"

class AddEditFragment : Fragment() {

    private val viewModel: TaskTimerViewModel by viewModels()

    interface OnSaveClicked {
        fun onSavedClick()
    }

    private var taskParam: Task? = null
    private var listener: OnSaveClicked? = null

    //Each activity is a context
    //We pass the activity which implements OnSavedClick as reference, because we cannot pas the Context as a parameter into a fragment.
    //If a fragment has any parameter it his constructor that parameter won't be executed when the configuration is changed /// NOT AS THAT -> Fragment(context)
    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: starts")
        super.onAttach(context)
        if (context is OnSaveClicked) {
            listener = context
        }
        else {
            throw RuntimeException(context.toString() + "must implement OnSavedClicked")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        taskParam = arguments?.getParcelable(ARG_TASK)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: starts")
        // Inflate the layout for this fragment here(VIEW)
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: starts")
        //WE WANT AS THIS METHOD TO HAPPEND ONLY AND ONLY IF THE BUNDLE IS NULL.- > first time when the fragment is called, when the bundle savedBUndleInstance is null
        //When the fragment is first called the OnSaveInstance wasen;t called so the savedInstaceState bundle is null
        //We don;t want as this method to be called again when we rotate the device, after we maybe modified the Fields
        //We'll leave that on the OnRestoreInstance State method to handle
        if (savedInstanceState == null) {
            //We have to declare the task here again, because the compiler doesn't know we didn;t modified the taskParam until now.
            val task = taskParam
            //If there was no recieve aprameter that means, the Fragment was launched without an task, in this particular case for ADD. the field will be empty -> we won;t execute anything
            //On the other side the if the parameter aren't null, That Means we called this fragment with EDIT Purpose, so there will be a task object, so the task object details will be stored into the fields
            if (task != null) {
                Log.d(TAG, "onViewCreated: Task details found, editing task ${task.id}")
                addEditName.setText(task.name)
                addEditDescription.setText(task.description)
                addEditSortOrder.setText(Integer.toString(task.sortOrder))
            }
            else {
                (Log.d(TAG, "onViewCreated: No arguments, adding new record"))
            }
        }

    }


    private fun taskFromUI(): Task {
        val sortOrder = if (addEditSortOrder.text.isNotEmpty()) {
            Integer.parseInt(addEditSortOrder.text.toString())
        }
        else {
            0
        }
        val newTask = Task(addEditName.text.toString(), addEditDescription.text.toString(), sortOrder)
        newTask.id = taskParam?.id ?: 0
        return newTask

    }

    fun isDirty():Boolean{
        val newTask=taskFromUI()
        //The function return true if the newTask is not equal with the task received from the bundle
        //if the tasks are equal the user hasn't made any changes, and the function return false
        //and also checking if the the name and description aren't empty, and the  sort order is change from the default 0
        return ((newTask!=taskParam) &&
                (newTask.name.isNotBlank() || newTask.description.isNotBlank() || newTask.sortOrder!=0 ))

    }

    //************************************************ ADD/EDIT TASK RECORD TO THE DATABASE(Insert and Update in sql) ************************************************************//
    private fun saveTask() {
        //Create a newTask object with the details to be saved, then call the viewModel's saveTask function to save it.
        //Task is now data class, so we can compare the new details with the original task and only save if they are different.
        val newTask = taskFromUI()
        //if the tasks are not equal
        if (newTask != taskParam) {
            Log.d(TAG, "saveTask: saving task, id is ${newTask.id}")
            //Currenly after saving a task we finish this fragment
            //but in the case we want to keep the fragment, and keep making changes.... in case our taskParam is null will have and id 0
            //When the id will be 0 instead of modifying the current task will keep create new task.
            //that is why we have to return the task from the view model class back to our taskParam -> In the case wee aDD a new Task to the db which didn't have any id(0L)
            taskParam = viewModel.saveTask(newTask)
            Log.d(TAG, "saveTask: saving task, id is ${newTask.id}")
        }


    }

//    //************************************************ ADD/EDIT TASK RECORD TO THE DATABASE(Insert and Update in sql) ************************************************************//
//    // Update the data field if at least one field changed(based on the task(record) values, and Insert a new field if the name field is not null
//    //-There's no need to hit the database unless this has happened.
//    private fun saveTask() {
//        val sortOrder = if (addEditSortOrder.text.isNotEmpty()) {
//            Integer.parseInt(addEditSortOrder.text.toString())
//        }
//        else {
//            0
//        }
//        //If any values in the widgets no longer match our original task, we'll add those values to the ContentValues.
//        val task = taskParam
//        val values = ContentValues()
//        //************************************************ UPDATE ************************************************************//
//        //if the task is not null means, means we want to update an existing record, based on his own field.
//        //check to see what field are changes, base on the current value(field)
//        if (task != null) {
//            Log.d(TAG, "saveTask: updating existing task")
//            //Adding new values if something changed
//            if (addEditName.text.toString() != task.name) {
//                values.put(TasksContract.Columns.TASK_NAME, addEditName.text.toString())
//            }
//            if (addEditDescription.text.toString() != task.description) {
//                values.put(TasksContract.Columns.TASK_DESCRIPTION, addEditDescription.text.toString())
//            }
//            if (sortOrder != task.sortOrder) {
//                values.put(TasksContract.Columns.TASK_SORT_ORDER, sortOrder)
//            }
//            //checking to see if we added something the to the ContentValues
//            if (values.size() != 0) {
//                Log.d(TAG, "saveTask:Updating task")
//
//                //update/edit based on the id
//                activity?.contentResolver?.update(
//                    TasksContract.buildUriFromId(task.id),
//                    values, null, null
//                )
//            }
//        }
//        //************************************************ ADD ************************************************************//
//        //if the task is null, the field start as being empty and also the task(retrieved from the bundle is null)
//        //we want to add a new record here, here will check only to see if the field are empty, we won't perform any other field checked base on task bundle(task) pass to this fragment
//        else {
//            Log.d(TAG, "saveTask: adding new task")
//            if (addEditName.text.isNotEmpty()) {
//                values.put(TasksContract.Columns.TASK_NAME, addEditName.text.toString())
//                //Because the description and sort order is not mandatory in our sql database(they are not NOT NULL), we will add them only if the name field is not empty
//                //No point to add them if the name field is empty, because the null field type is(NOT NULL), so our record(row) in the sql database must have a name
//                if (addEditDescription.text.isNotEmpty()) {
//                    values.put(TasksContract.Columns.TASK_DESCRIPTION, addEditDescription.text.toString())
//                }
//                //because we processed before the order data, so in case it field is empty, the given value will be 0
//                values.put(TasksContract.Columns.TASK_SORT_ORDER, sortOrder)
//                activity?.contentResolver?.insert(TasksContract.CONTENT_URI, values)
//            }
//        }
//
//    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: starts")

        addEditBtnSave.setOnClickListener {
            saveTask()
            listener?.onSavedClick()
        }
        //Action Bar
        if (listener is AppCompatActivity) {
            val actionBar = (listener as AppCompatActivity?)?.supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")
        super.onStart()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored called")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume called")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onPause called")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState called")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach called")
        super.onDetach()
    }

    //This function represent a convenient way to pass a task to the fragment, without having to include all that bundle code
//We cannot pass the task argument in the constructor, but that is not possible with the fragments, parameter have to have parameter less constructor
//-> because if a fragment gets destroyed, Android will recreate it, so that takes care of saving the fragment state for us, but id does not have idea what
//argument we passed when we created it.
//If we pass parameter to Fragment constructor, they won;t be available when Android recreates the fragment
    companion object {
        fun newInstace(task: Task?) =
            AddEditFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_TASK, task) }
            }
    }

}

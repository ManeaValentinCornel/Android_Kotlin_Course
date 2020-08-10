package com.vcmanea.a11_tasktimer_app.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vcmanea.a11_tasktimer_app.*
import com.vcmanea.a11_tasktimer_app.dialogs.AppDialog
import com.vcmanea.a11_tasktimer_app.dialogs.SettingsDialog
import com.vcmanea.a11_tasktimer_app.fragments.addEditFragment.AddEditFragment
import com.vcmanea.a11_tasktimer_app.fragments.mainFragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"
private const val DIALOG_ID_CANCEL_EDIT = 1

class MainActivity : AppCompatActivity(),
    AddEditFragment.OnSaveClicked, MainFragment.OnTaskEdit, AppDialog.DialogEvents {

    //Whether or the activity is in 2-pane mode
    //i'e. running on the tablet, or on lanscape
    private var mTwoPane = false


    //module scope because we need to dismiss it in onStop,(e.g. when orientation changes) to avoid memory leaks.
    //That is the reason we have to declare the about dialog as a member variable, to be able to dismiss it, before on destroyMethod is called in order to avoid memory leaks

    private var aboutDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //******************************************************* FRAGMENTS BEHAVIOUR STARTS HERE ****************************************************************//
        //CHECK THE ORIETNATION
        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment != null) {
            //EDIT_ADD FRAGMENT IS NOT NULL, SO THE EDIT_ADD_MAIN_FRAGMENT function was launch-> will be visible now matter what
            task_details_container.visibility = View.VISIBLE
            //In PORTRAIT the as the EDIT_ADD_MAIN_FRAGMENT to be seen has to be on all screen... SO the MAIN_FRAGMENT wil be GONE
            //In LANDSCAPE both will be visible
            mainFragment.view?.visibility = if (mTwoPane) View.VISIBLE else View.GONE
        }
        else {
            //THE EDIT_ADD_FRAGMENT is NULL, si the edit_add operation was not required
            //->so in LANDSCAPE will be GONE(THE OTHER FRAGMENT WILL SHOW ON ALL SCREEN) otherwise LANDSCAPE will be (INVISIBLE) the other fragment will show half screen
            task_details_container.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
            //result that mainFragment has to be visible
            mainFragment.view?.visibility = View.VISIBLE
        }
        Log.d(TAG, "onCreate ends")
    }

    //THIS METHOD IS USED
    private fun showEditPane() {
        //There was an existing fragment as editAddFragment, make sure the panes are set correctly
        task_details_container.visibility = View.VISIBLE
        //hide the left hand pane, if in single pane view
        mainFragment.view?.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }

    private fun showAboutDialog() {
        //Dialogues aren't part of the underlying activity, they display on top of it and are styled separately.
        //So there is not root viiew we can sensibly use, that is why we pass null
        val messageView = layoutInflater.inflate(R.layout.about, null, false)
        val builder = AlertDialog.Builder(this)
        //It is a must as the title and the icon the pe set before we create the create() method
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)

        //Setting the listener on the view rather than on the button
        //when the view will be tapped it will get dismiss
        //the problem is the icon and the title are not part of the view, they are added by the dialog builder, so when we tapped that nothing happens
//        messageView.setOnClickListener({
//            Log.d(TAG,"Enterong messageView.onClick")
//            if(aboutDialog!=null && aboutDialog?.isShowing==true){
//                aboutDialog?.dismiss()
//            }
//        })
        //--> better way
        builder.setPositiveButton(R.string.ok) { _, _ ->
            Log.d(TAG, "onClick: Entering messageView.onClick")
            if (aboutDialog != null && aboutDialog?.isShowing == true) {
                aboutDialog?.dismiss()
            }

        }



        aboutDialog = builder.setView(messageView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)


        val aboutVersion = messageView.findViewById(R.id.about_version) as TextView
        aboutVersion.text = BuildConfig.VERSION_NAME


        //Use a nullable type: the TextView win't exist on API 21 and higher
        //on the api higer than 21 the R.id.about_url won;t be found. so the onCLickListener won't be found
        val aboutUrl: TextView? = messageView.findViewById(R.id.about_url)
        aboutUrl?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = (it as TextView).text.toString()
            intent.data= Uri.parse(uri)
            startActivity(intent)


        }


        //We'll have a memory leak if we are not dismissing our dialog
        //A memory leak occurs when java mechanism can't reclaim the memory and other resources, that an object uses.
        //We should dismiss the dialog when our activity is destroyed by android
        //A good place to do that is in the onStop
        aboutDialog?.show()


    }

    //SHOW THE ADD_EDIT_FRAGMENT TASK
    private fun taskEditRequest(task: Task?) {
        Log.d(TAG, "taskEditRequest: starts")
        // Create a new fragment to edit the task, passing the parameter directly -> also is a must to implement the call back method
        val newFragment = AddEditFragment.newInstace(task)
        supportFragmentManager.beginTransaction()
            .replace(R.id.task_details_container, newFragment)
            .commit()
        showEditPane()
    }

    //CALL BACK FROM THE EDIT FRAGMENT -> WHen the SAVE Button is pressed, the ADD_EDIT will be removed.
    //-> And The Main_FRAGMENT(TASK_LIST) WILL BE SHOW
    override fun onSavedClick() {
        Log.d(TAG, "OnSaveClick: called")
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        removeEditPaneFragment(fragment)
    }

    //WHEN REMOVE THE EDIT_TEXT_FRAGMENT THE MAIN ACTIVITY WILL BE SHOWN INSTEAD
    private fun removeEditPaneFragment(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane called")
        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        //Set the visibility of the right hand pane
        task_details_container.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
        // and show the left hand pane
        mainFragment.view?.visibility = View.VISIBLE

        //Remove the action bar home button when the EditText operation is removed or it has been completed
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


    }
    //******************************************************* FRAGMENTS BEHAVIOUR END HERE ****************************************************************//

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menumain_addTask -> taskEditRequest(null)

            R.id.menumain_showSettings->{
                val dialog=SettingsDialog()
                dialog.show(supportFragmentManager,null)
            }

            R.id.menumain_showAbout -> showAboutDialog()

            //HomeButton functionality, the id is declared in android package
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: home button pressed")
                val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
                //removeEditPaneFragment(fragment)

                //If the function is dirty, more specific if it has been modified, prompt the dialog from the extension function
                if ((fragment is AddEditFragment) && fragment.isDirty()) {
                    //ExtensionFunction-> used to attach the dialog
                    showConfirmationDialog(
                        DIALOG_ID_CANCEL_EDIT,
                        "You have unsaved changes. Do you wanto to abandon you chages, or continue editing?",
                        R.string.canelEditDiag_message,
                        R.string.continueEditDiag_message
                    )
                }
                else {
                    removeEditPaneFragment(fragment)
                }
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        Log.d(TAG, "onResume starts")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart starts")
        super.onStart()
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment == null || mTwoPane)
            super.onBackPressed()
        else
        //WHen back button is pressed in Portrait and the EditAddFragment is on,it will get canceled and only after the regular behaviour of obBackPressed will be used
        //removeEditPaneFragment(fragment)
        //If the function is dirty, more specific if it has been modified, prompt the dialog from the extension function
            if ((fragment is AddEditFragment) && fragment.isDirty()) {
                //ExtensionFunction-> used to attach the dialog
                showConfirmationDialog(
                    DIALOG_ID_CANCEL_EDIT,
                    "You have unsaved changes. Do you wanto to abandon you chages, or continue editing?",
                    R.string.canelEditDiag_message,
                    R.string.continueEditDiag_message
                )
            }
            else {
                removeEditPaneFragment(fragment)
            }
    }

    override fun onPostiveDialogResult(dialogId: Int, args: Bundle) {
        Log.d(TAG, "onPositiveDialogResult: called with dialogId $dialogId")
        if (dialogId == DIALOG_ID_CANCEL_EDIT) {
            val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
            removeEditPaneFragment(fragment)
        }
    }

    override fun onTaskEdit(task: Task) {
        Log.d(TAG, "onTaskEdit called")
        taskEditRequest(task)

    }

    //Used to dismiss the aboutDialog is case it is showing, before the onDestroy method is called
    override fun onStop() {
        if (aboutDialog?.isShowing == true) {
            aboutDialog?.dismiss()
        }
        super.onStop()
    }


    //    //************************************************************TESTING CONTENT PROVIDER ************************************************************************//
//    //*****************************************************************QUERY ************************************************************************//
//    private fun testQuery() {
//        // val projection = arrayOf(TasksContract.Columns.TASK_NAME, TasksContract.Columns.TASK_SORT_ORDER) //-> Instead of "SELECT [*] FROM" the query with the projection will be ["SELECT Name,SortOrder] FROM"
//        val sortOrder = TasksContract.Columns.TASK_SORT_ORDER //-> ORDER BY CLAUSE
//        val cursor = contentResolver.query(
//            //TasksContract.buildUriFromId(2), // selecting a single row by Converting the uri to a integer
//            TasksContract.CONTENT_URI, //-> get all the data
//            null,
//            null,
//            null,
//            sortOrder
//        )
//        cursor?.use {
//            while (it.moveToNext()) {
//                with(cursor) {
//                    val id = getLong(0)
//                    val name = getString(1)
//                    val description = getString(2)
//                    val sortOrder = getString(3)
//
//                    val result = "id=$$id Name: $name, description:$description sort order: $sortOrder"
//                    Log.d(TAG, "onCreate:reading data $result")
//                }
//            }
//        }
//    }
//
//    //************************************************************INSERT ************************************************************************//
//    private fun testInsert() {
//        val values = ContentValues().apply {
//            put(TasksContract.Columns.TASK_NAME, "New Task 1")
//            put(TasksContract.Columns.TASK_DESCRIPTION, "Description 1")
//            put(TasksContract.Columns.TASK_SORT_ORDER, 2)
//        }
//        val uri = contentResolver.insert(TasksContract.CONTENT_URI, values)
//        Log.d(TAG, "New row id (in uri_ is $uri")
//        if (uri != null)
//            Log.d(TAG, "id for inserted uri is ${TasksContract.getIdFromUri(uri)}")
//    }
//
//    //************************************************************UPDATE ************************************************************************//
//    private fun testUpdate() {
//        val values = ContentValues().apply {
//            put(TasksContract.Columns.TASK_NAME, "Korlin")
//            put(TasksContract.Columns.TASK_DESCRIPTION, "Android Kotlin course ")
//            put(TasksContract.Columns.TASK_SORT_ORDER, 1)
//        }
//        val taskUri = TasksContract.buildUriFromId(4)
//        val rowAffected = contentResolver.update(taskUri, values, null, null) //-> Updating base on the row URI
//
//        Log.d(TAG, "id affected by update is $rowAffected")
//    }
//
//    //************************************************************UPDATE BULK ************************************************************************//
//
//    private fun testUpdateBulk() { //-> Bulk means update more than one entry at a time
//        val values = ContentValues().apply {
//            put(TasksContract.Columns.TASK_NAME, "AAAAAAAAA")
//            put(TasksContract.Columns.TASK_DESCRIPTION, "AAAAAAAAAAA")
//            put(TasksContract.Columns.TASK_SORT_ORDER, 99)
//        }
//        //val selection = TasksContract.Columns.TASK_SORT_ORDER + " = 99"  // -> Simple Where clause which doesn' which doesn't stop sql injectio na attacks.
//        val selection = TasksContract.Columns.TASK_SORT_ORDER + " = ?" // -> avoid sql injection attacks, when user enter data manually
//        val selectionArgs = arrayOf("99")/// -> avoid sql injection attacks, when user enter data manually
//        val rowAffected = contentResolver.update(TasksContract.CONTENT_URI, values, selection, selectionArgs) // Update base on the table URI
//        Log.d(TAG, "row affected by update $rowAffected")
//
//    }
//    //************************************************************ DELETE BULK ************************************************************************//
//    private fun testDeleteBulk() { //-> Bulk means update more than one entry at a time
//
//        val selection = TasksContract.Columns.TASK_SORT_ORDER + " = ?" // -> avoid sql injection attacks, when user enter data manually
//        val selectionArgs = arrayOf("99")/// -> avoid sql injection attacks, when user enter data manually
//        val rowAffected = contentResolver.delete(TasksContract.CONTENT_URI, selection, selectionArgs) // Update base on the table URI
//        Log.d(TAG, "row affected by deletion $rowAffected")
//
//    }


}

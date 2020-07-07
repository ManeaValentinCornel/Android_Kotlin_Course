package com.vcmanea.contactproviderapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


private const val TAG = "MainActivity"
private const val REQUEST_CODE_READ_CONTACT = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //***************************************** PERMISSION *****************************************///
        fab.setOnClickListener { view ->
            Log.d(TAG, "fab onClick: starts")
            //PERMISSION ALREADY GRANTED
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                queryContentProvider()
            }
            //PERMISSION NOT GRANTED
            else {
                Snackbar.make(view, "Please grant access to your Contacts", Snackbar.LENGTH_LONG).setAction("GRANT ACCESS", {
                    //PERMISSION WAS DENIED PERVIOUSLY
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        requestpermission()
                    }
                    //PERMISSION WAS PERMANENTLY DENIED BEFORE
                    else {
                        //Start settings intent
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        this.startActivity(intent)
                    }

                }).show()

            }
        }
        Log.d(TAG, "onCreate ends")
    }

    //***************************************** ON REQUEST PERMISSION RESULT *****************************************///
    //1 First parameter the requestCode defined by us
    //2 The second parameter is a list with the permission name
    //3 Result for each permission, from parameter 2
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionResult")
        //NEVER TRY TO SAVE THE STATE INTO A BOOLEAN VARIABLE VARIABLE
        when (requestCode) {
            REQUEST_CODE_READ_CONTACT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission granted")
                    //permission was granted
                }
                else {
                    Log.d(TAG, "onRequestPermissionsResult: permission refused")
                    //permission denies!! Disable the functionality that depends on this permission
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: ends")
    }

    //***************************************** REQUEST PERMISSION*****************************************///
    private fun requestpermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACT)
    }

    fun queryContentProvider() {
        //***************************************** CONTENT RESOLVER -> QUERY *****************************************///
        //CONTENT RESOLVER
        //Responsible for resolving all the request for content by directing the request to the appropriate Content Provider. An application has a single content Resolver which provides access to all the ContentProviders that exists on the device.
        //Main activity request contact data from the Content Resolver.MainActivity uses the URI to specify tyhe kind of data it want
        //ContentResolver then uses the URI to decide which ContentProvider it should  ask to supply the data.ContentResolver doesn't know where the Content Provider will get the data from
        //The ContentProvider fetches the data from its Data Source.In our app the ContentProvider goes to the Cotnact database to get the contacts records taht have been requested.Content resolver query function against it data source and return a cursor

        //PARAMETERS
        //1 First parameter is tue URI, which identifies the data source where we want to ge the data from
        //2 Second parameter is the projection, which is a string array holding the name of the columns that we want to retrieve
        val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        //3 Selection, is pretty much like the 'where' clause in a sql statement, exactly what it is but without the SQL keyword 'where'
        //4 ?
        //5 Sort order,based on what column
        //CONTENT RESOLVER
        //When we call contentResolver.query, the contentResolver extract the Authority
        //->from the URI and uses that to decide which Content Provider it should send the query request to.
        //And taht gets a cursor back from the ContentProvider
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )
        //LIST VIEW WITH ADAPTER
        val contacts = ArrayList<String>()
        cursor?.use {
            while (it.moveToNext()) {
                contacts.add(it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)))
            }
        }
        val adapter = ArrayAdapter<String>(this, R.layout.contact_row, R.id.name, contacts)
        listView.adapter = adapter
        Log.d(TAG, "fab on click: ends")
    }
}

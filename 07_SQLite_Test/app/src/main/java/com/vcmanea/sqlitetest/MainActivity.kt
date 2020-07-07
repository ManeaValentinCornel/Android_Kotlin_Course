package com.vcmanea.sqlitetest

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //**************************************************SQL DATABASE ****************************************************************//
        //Try to open the database, but if there isn't already a database called sqlite-test-1.db it will automatically create one for us.
        //Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        //One reason for using insert and update,is that we have no record of the id that was generated for the new row
        val database = baseContext.openOrCreateDatabase("sqlite-test-1.db", Context.MODE_PRIVATE, null)
        database.execSQL("DROP TABLE IF EXISTS contacts")

        //************************************************** EXEC SQL****************************************************************//
        var sql = "CREATE TABLE IF NOT EXISTS contacts(_id INTEGER PRIMARY KEY NOT NULL, name TEXT, phone INTEGER, email TEXT)"
        database.execSQL(sql)
        sql = "INSERT INTO contacts(name,phone,email) VALUES ('Bobo',112344,'bobo@gmail.com')"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)

        //************************************************** INSERT ****************************************************************//
        //Wrapper class, around the hash map, that allows us to store a set of Key/Value pairs
        val values = ContentValues().apply {
            put("name", "Fred")
            put("phone", 12345)
            put("email", "fredy@gmail.com")
        }
        //With the insert method you can retrieve the primary key generated, wehn you added a record to the database
        val generatedId = database.insert("contacts", null, values)

        //************************************************** QUERY-CURSOR  ****************************************************************//
        //The rawQuery method return a cursor
        //In database terms, a cursor allows us to access individual records in the database.We can move backwards and forwards in the cursor, and it take care of retrieving the rows for us.
        // In database a cursor may return thousand of rows, and that could be a problem because smart [hones have limited ampunts of memory. All that data, possibly wouldn't fit in the memory we've got available->
        //But the cursor takes care of this and points to just a single row at a time. So we can access and manipulate the data without having to load it all into memory.
        //Allows us to manipulate the data but only retrieves it one record at a time.
        val query = database.rawQuery("SELECT * FROM contacts", null)


        //************************************************** USE FUNCTION -> CLOSES THE RESOURCES > uses a receiver(it)****************************************************************//
        //This function can be used instead of try catch block, so execute a block with resources and closes it down correctly whether an exception is thrown or not
        query.use {
            //Always check if there is a first element before to move the cursor to the next element
            while (it.moveToNext()) {
                //Cycle through all the records
                with(it) {
                    val id = getLong(0)
                    val name = getString(1)
                    val phone = getString(2)
                    val email = getString(3)
                    val result = "ID= $id, name=$name, phone=$phone email=$email"
                    Log.d(TAG, "onCreate: reading data $result")
                }
            }

        }
        database.close()
        Log.d(TAG, "onCreate: record added with the id $generatedId")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

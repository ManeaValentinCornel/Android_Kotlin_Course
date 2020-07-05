package com.vcmanea.flickerapp

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView


class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"
    private var searchView: androidx.appcompat.widget.SearchView? = null

    //Search manager retrieves the searchable configuration, that we've defined in Search activity manifest,
    //and associate it with the search view widget that embedded in our toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //**************************************** BASE ACTIVITY TOOLBAR***********************************************//
        activateToolbar(true)
        Log.d(TAG, ".onCreate ends")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreatOptionMenu: starts")
        //**************************************** SEARCH VIEW ***********************************************//
        menuInflater.inflate(R.menu.menu_search, menu)
        //The search manager provides access to the system search services.
        //Then we get a reference of the search view widget that's embedded in the search menu item of the toolbar
        //We're getting the search manager to retrieve the searchable info from searchable.xml by calling getSearchableInfo function
        //We need to provide the getSearchableInfo with the component name of the activity that we want the information for
        //The searchable info is then set into the search view widget to configure it

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)
        Log.d(TAG, "onCreatOptionMenu: $componentName")
        Log.d(TAG, "onCreatOptionMenu: hint is  ${searchView?.queryHint}")
        Log.d(TAG, "onCreatOptionMenu: hint is  $searchableInfo")

        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "setOnQueryTextListener: called")

                //**************************************** SHARED PREFERENCES***********************************************//
                val shareRef = applicationContext.getSharedPreferences(getString(R.string.shared_preferences), (Context.MODE_PRIVATE))
                //val editor=shareRef.edit()
                with(shareRef.edit()) {
                    putString(getString(R.string.shared_flicker_query), query)
                    apply()
                }
                //Strange behavior if we leave taht out with devices which have keyboard connected
                //You only see the different behavior when you press enter on a externale keyboard
                searchView?.clearFocus()

                finish()
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        Log.d(TAG, "onCreatOptionMenu: starts")
        return true
    }
}




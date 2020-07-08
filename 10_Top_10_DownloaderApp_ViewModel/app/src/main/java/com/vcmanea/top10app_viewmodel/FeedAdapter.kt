package com.vcmanea.top10app_viewmodel;
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedAdapter(context: Context, private val resource: Int, private var applications: List<FeedEntry>) :
    ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)


    fun setFeedList(feedList: List<FeedEntry>) {
        this.applications=feedList
        notifyDataSetChanged()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        //convertview get reused when is not null.
        if (convertView == null) {
//            Log.d(TAG, "getView() called with null covnertView")
            view = inflater.inflate(resource, parent, false)
            //creating a new ViewHolder object and store it in the view's tag using the setTag method
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }
        else {
//            Log.d(TAG, "getView() called with covnertView")
            //retrieveing the viewHolder from it tag by usign getTag method
            view = convertView
            //TAG is an object, so we have to cast it to a view holder
            //Android doesn't use this TAG object so whatever will be store in TAG won;t get touch by ANDORID
            viewHolder = view.tag as ViewHolder
        }
        //By using the ViewHOlder pattern , this heavy operations aren't required anymore.
        // The view references is stored in the viewholder object which is storent in the tag object
//        val tvName: TextView = view.findViewById(R.id.tvTitle)
//        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
//        val tvSummary: TextView = view.findViewById(R.id.tvSummary)

        val currentApp = applications[position]
        viewHolder.tvName.text = currentApp.title
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary
        return view
    }

    override fun getCount(): Int {
   Log.d(TAG, "getCount() called $applications.size")
        return applications.size
    }

    class ViewHolder(view: View) {
        val tvName: TextView = view.findViewById(R.id.tvTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
    }


}
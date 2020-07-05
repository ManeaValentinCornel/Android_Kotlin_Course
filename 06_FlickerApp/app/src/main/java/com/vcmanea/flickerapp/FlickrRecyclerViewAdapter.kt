package com.vcmanea.flickerapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickerImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickerImageViewHolder>() {
    //IMPORT TO KNOW A LOG tag can be the maximum of 23 characters
    private val TAG = "FlickrRecyclerViewAdapt"

    override fun getItemCount(): Int {
        Log.d(TAG, ".getItemCount called")
        //very important to return 1 instead of 0 in order to see the placeholder photo if there is no tag matching the query
        return if (photoList.isNotEmpty()) photoList.size else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickerImageViewHolder {
        //Called by the layout manager when it need a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_row, parent, false)
        //A common error is the null parameter to be passed to the,instead of the parent...
        //without knowing the parent view, the inflater, will don't know things as waht theme should be apply,widget styles will be default ->
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.browse_row,null)
        return FlickerImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickerImageViewHolder, position: Int) {
        //IF no photos match the tags that we search for, we get the placeholder iamge and message but is very important as itemGetCount to return 1 instead of 0.
        if (photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.empty_query)
        }
        else {
            //Called by the layout managerwhen it want new data in a existing ViewHolder
//        Log.d(TAG, "onBindViewHolder new view requested")
            val photoItem = photoList[position]
            //Picasso will download the image from the URL on a background thread, and puts it into the image view once it's downloaded
            Picasso.get().load(photoItem.imageUrlSmall).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.thumbnail)
            holder.title.text = photoItem.title
        }
    }

    //THIS FUNCTION TAKE THE NEW LIST AS A PARAMETER AND STORE IT THE PHOTO LIST FIELD
    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        //the notifyDataSetChanged tells the recycler view that the data has changed, so that it can refresh the display
        notifyDataSetChanged()
    }

    fun getPhotoDetails(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }
}
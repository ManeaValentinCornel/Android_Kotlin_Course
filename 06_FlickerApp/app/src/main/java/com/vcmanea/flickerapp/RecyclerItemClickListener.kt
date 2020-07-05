package com.vcmanea.flickerapp

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
//MORE ABOUT THAT ON TIM BUCHALKA 150-151.
class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener) :
    RecyclerView.SimpleOnItemTouchListener() {
    private val TAG = "RecyclerItemClickListen"
    //**************************************** CALL BACKS ***********************************************//
    interface OnRecyclerClickListener {
        fun onItemClick(view: View?, position: Int?)
        fun onItemLongClick(view: View?, position: Int?)
    }

    //**************************************** ADD GESTURE DETECTOR ***********************************************//
    //-> If the gesture detector consumes(handle) the event(motionEvent) will return true, otherwise false.
    //-> Exception-> onLongPress and onShowPress functions don;t return a value

    private val gestureDetector= GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener(){
        //Reading the documentation, we may consider using onSingleTapConfirmed -> WHEN WE USE DOUBLE TAP, because if we already consumed a single tap will miss the second one
        //-->onSingleTapConfirmed Unlike onSingleTapUp(MotionEvent), this will only be called after the detector
        //-->is confident that the user's first tap is not followed by a second tap leading to a double-tap gesture.
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapUp: starts")
            //Finding the childView. ChildView is the view from the recyclerView which was tappedUp
            val childView=recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG, "onSingleTapUp: calling listener.onItemClick")
            listener.onItemClick(childView, childView?.let { recyclerView.getChildAdapterPosition(it) })
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            //The rationale behind onLongPress and on ShowPres methods on not returning a boolean is because when we longPress a view, we can still scroll down
            //if it was returning true we would't be able. We saw` when we return true in onInterceptTouchEvent we won't be able to scroll anymore
            Log.d(TAG, "onLongPress: starts")
            //Finding the childView. ChildView is the view from the recyclerView which was tappedUp
            val childView=recyclerView.findChildViewUnder(e.x,e.y)
            Log.d(TAG, "onLongPress: onItemLongClick")
            listener.onItemLongClick(childView, childView?.let { recyclerView.getChildAdapterPosition(it) })
        }
    })


        //**************************************** onInterceptTouchEvent ***********************************************//
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        //1 onInterceptTouchEvent
        //Every time we click the screen this method is called twice, once for the tap and secondly for the release :onInterceptTouchEvent: starts MotionEvent{}
        //If we scroll the :onInterceptTouchEvent: starts MotionEvent{} will be colled multiple times. Also will get some action moves as: action=ACTION_MOVE,action=ACTION_UP...
        Log.d(TAG, "onInterceptTouchEvent: motionEvent is $e")
//return true
        //If we return true here, the recycler view won't be able to scroll to the list. so we won't get any action moves either
        //Because by returning true, we intercepted the touch event, and told the system we handled every single event
        //So in other words that tell Android that nothing else has to handle those event, that is what happens when we return true
        //So we can intercept only what events we want, and leave the system do everything what it wants, whit the ones we don't have anything to do with ->  return super.onInterceptTouchEvent(rv, e)
        //When we let an event through the  return super.onInterceptTouchEvent(rv, e) will be called. SO the android system will know what to do further with the event in case something from the system is listen for

        //**************************************** GESTURE DETECTOR ***********************************************//
        //2 INTERCEPT motionEvents with GESTURE DETECTOR(return true if the motion event is "Consumed")
        //-->GESTURE DETECTOR can tell what type of gesture in happening , so we will know what events we want to intercept and what event we want to let through -> return super.onInterceptTouchEvent(rv, e)
        //-->GESTURE DETECTOR will use as parameter the motion event, from the onInterceptTouchEvent.
        //-->So the theory is that everything that gesture GESTURE DETECTOR deals with return true. anything which doesn't handle should return false so taht something else can deal with it
        val result=gestureDetector.onTouchEvent(e)
        Log.d(TAG, "onInterceptTouchEvent(): returning $result")
        return super.onInterceptTouchEvent(rv, e)
    }
}
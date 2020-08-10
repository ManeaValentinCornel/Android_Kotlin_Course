package com.vcmanea.a11_tasktimer_app.adapters

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vcmanea.a11_tasktimer_app.R
import com.vcmanea.a11_tasktimer_app.Task
import com.vcmanea.a11_tasktimer_app.TasksContract
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_list_item.view.*
import java.lang.IllegalStateException

private const val TAG = "CursorRecyclerViewAdapt"


class TaskViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(task: Task,listener:CursorRecyclerViewAdapter.OnTaskClickListener) {
        containerView.tliName.text=task.name
        containerView.tliDescription.text=task.description
        containerView.tliEditBtn.visibility=View.VISIBLE
        containerView.tliDeleteBtn.visibility=View.VISIBLE

        containerView.tliEditBtn.setOnClickListener{
            Log.d(TAG,"edit button tapped. task name is ${task.name}")
            listener.onEditClicked(task)
        }
        containerView.tliDeleteBtn.setOnClickListener{
            Log.d(TAG,"delete button tapped. task name is ${task.name}")
            listener.onDeleteClicked(task)
        }

        containerView.setOnLongClickListener{
            Log.d(TAG,"onLongClick: task name is ${task.name}")
            //The on Long click listener need to return true if it has handled the tap
            listener.onTaskLongClicked(task)
            true
        }

    }
}

class CursorRecyclerViewAdapter(private var cursor: Cursor?,private val listener:CursorRecyclerViewAdapter.OnTaskClickListener) :
    RecyclerView.Adapter<TaskViewHolder>() {

    interface OnTaskClickListener{
        fun onEditClicked(task:Task)
        fun onDeleteClicked(task:Task)
        fun onTaskLongClicked(task:Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.d(TAG, "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: starts")
        val cursor = cursor       //avoid problems with smart cast
        if (cursor == null || cursor.count == 0) {
            Log.d(TAG, "onBindViewHolder: provinding instructions")
            holder.containerView.tliName.setText(R.string.empty_instruction_title)
            holder.containerView.tliDescription.setText(R.string.empty_instruction_description)
            holder.containerView.tliEditBtn.visibility = View.GONE
            holder.containerView.tliDeleteBtn.visibility = View.GONE
        }
        else {
            //there is not such position as that in the cursor-> will return false
            if (!cursor.moveToPosition(position)) {
                throw IllegalStateException("Couln't move cursor to position $position")
            }
            else {

                //Create a Task object from the data Cursor
                val task = Task(
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASK_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksContract.Columns.TASK_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(TasksContract.Columns.TASK_SORT_ORDER))
                )
                //Very important to remember is the id is not set into the constructor
                task.id = cursor.getLong(cursor.getColumnIndex(TasksContract.Columns.ID))

             holder.bind(task,listener)

            }
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: starts")
        //The recyclerView uses this so it knowh how many items there are to display. If there are not items, we will be sending back a view that displays the i nstructions.
        //So we dont' wat to tell the recycler view that there are no record, we will always return at least one
        val cursor = cursor
        val count = if (cursor == null || cursor.count == 0) {
            //fib, because we populate a single ViewHolder with instructions
            1
        }
        else {
            cursor.count
        }

        return count
    }

    /**
     * Swap in a new Cursor, return the old Cursor
     * The returned old cursor is *not* closed.
     *
     * @param newCursor The new Cursor to be used
     * @return Return the previously set Cursor, or null if there wasn't
     */
    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        //save the current item count from the rv
        val numItem = itemCount
        val oldCursor = cursor
        cursor = newCursor
        if (newCursor != null) {
            //notify the observers about the new cursor
            notifyDataSetChanged()
        }
        else {
            //notify the observers about the alack of a data set
            notifyItemRangeRemoved(0, numItem)
        }
        return oldCursor

    }
}



package com.vcmanea.a11_tasktimer_app.fragments.mainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vcmanea.a11_tasktimer_app.*
import com.vcmanea.a11_tasktimer_app.adapters.CursorRecyclerViewAdapter
import com.vcmanea.a11_tasktimer_app.dialogs.AppDialog
import com.vcmanea.a11_tasktimer_app.dialogs.DIALOG_ID
import com.vcmanea.a11_tasktimer_app.dialogs.DIALOG_MESSAGE
import com.vcmanea.a11_tasktimer_app.dialogs.DIALOG_POSITIVE_RID
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.AssertionError
import java.lang.IllegalStateException

private const val TAG = "MainFragment"
private const val DIALOG_ID_DELETE = 1
private const val DIALOG_TASK_ID = "task_id"

class MainFragment : Fragment(), CursorRecyclerViewAdapter.OnTaskClickListener,
    AppDialog.DialogEvents {

    interface OnTaskEdit {
        fun onTaskEdit(task: Task)
    }

    private var onTaskListener: OnTaskEdit? = null


    //adapter
    private val mAdapter = CursorRecyclerViewAdapter(null, this)

    //subscribe to the viewModel
    private val viewModel: TaskTimerViewModel by viewModels()

    //Each activity is a context
    //We pass the activity which implements OnSavedClick as reference, because we cannot pas the Context as a parameter into a fragment.
    //If a fragment has any parameter it his constructor that parameter won't be executed when the configuration is changed /// NOT AS THAT -> Fragment(context)
    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach called")
        super.onAttach(context)

        if (context is OnTaskEdit) {
            onTaskListener = context
        }
        else {
            throw IllegalStateException(context.toString() + "must implement onTaskListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        viewModel.cursor.observe(this, Observer { cursor -> mAdapter.swapCursor(cursor) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)
        taskRv.layoutManager = LinearLayoutManager(context)
        taskRv.adapter = mAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated called")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewStateRestored called")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")
        super.onStart()
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
        Log.d(TAG, "onStop called")
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

    override fun onEditClicked(task: Task) {
        //\NOT GOOD BEHAVIOUR
        //call the function from the MainActivtity
        onTaskListener?.onTaskEdit(task)
    }

    override fun onDeleteClicked(task: Task) {
        //create new AppDialog instance and show it
        val args = Bundle().apply {
            putInt(DIALOG_ID, DIALOG_ID_DELETE)
            putString(DIALOG_MESSAGE, getString(R.string.del_diag_message, task.id, task.name))
            putInt(DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption)
            putLong(DIALOG_TASK_ID, task.id) //pass the id in the argumets, so we can retrieve it when we get called back.
        }
        val dialog = AppDialog()
        dialog.arguments = args
        //childFragmentManager manager when you show a dialog from a fragment, and supportFragmentManager when you show a fragment from an activity
        dialog.show(childFragmentManager, null)

    }

    override fun onTaskLongClicked(task: Task) {
        TODO("Not yet implemented")
    }

    override fun onPostiveDialogResult(dialogId: Int, args: Bundle) {
        Log.d(TAG, "onPostiveDialogResult: called wit id $dialogId")

        //call the deleteTask from the viewModel class
        if (dialogId == DIALOG_ID_DELETE) {
            val taskId = args.getLong(DIALOG_TASK_ID)
            //Instead throwing an exception we can use an assertion, BuildConfig.Debug is a system-wide constant that the compiler can use.
            //When you produce the release version of you app, BUildConfig.debug will be false and this code won;t compile in the final app
            if(BuildConfig.DEBUG && taskId==0L) throw AssertionError("Task ID is zero")
            viewModel.deleteTask(taskId)
        }
    }


}






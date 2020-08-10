package com.vcmanea.a11_tasktimer_app.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDialogFragment
import com.vcmanea.a11_tasktimer_app.R
import java.lang.ClassCastException
import java.lang.IllegalArgumentException

private const val TAG = "AppDialog"

const val DIALOG_ID = "id"
const val DIALOG_MESSAGE = "message"
const val DIALOG_POSITIVE_RID = "positive_rid"
const val DIALOG_NEGATIVE_RID = "negative"

class AppDialog : AppCompatDialogFragment() {

    //******************************************************* CALLBACK INTERFACE ****************************************************************//
    private var dialogEvents: DialogEvents? = null

    /**
     * The dialogue's callback interface, to notify of user selected results (deletion confirmed,
     */
    internal interface DialogEvents {
        fun onPostiveDialogResult(dialogId: Int, args: Bundle)
//        fun onNegativeDialogResult(dialog: Int, args: Bundle)
//         fun onDialogCancelled(dialogId: Int)
    }

    //******************************************************* ATTACH -> handling the interface callback ****************************************************************//
    //Each activity is a context
    //We pass the activity which implements OnSavedClick as reference, because we cannot pas the Context as a parameter into a fragment.
    //If a fragment has any parameter it his constructor that parameter won't be executed when the configuration is changed /// NOT AS THAT -> Fragment(context)
    //First try cast the parentFragment to be a Dialogue events. If there is no parent fragment, then it will be null, and we'll get a typecast exception
    //If that happens, we shlod have a context, the Activity, and will try to cast that as DialogEvents interface
    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach called: context is $context")
        super.onAttach(context)
        //Activities/Fragments containing this fragment must implement its callbacks.
        dialogEvents = try {
            //Is there a parent fragment? If so, that will be what we call bag
            parentFragment as DialogEvents

        }
        //No fragment, it is a activity
        catch (e: TypeCastException) {
            try {
                //No parent fragment, so call back the Activity instead
                context as DialogEvents
            }
            //If that fails means the activity doesn't implements the interface
            catch (e: ClassCastException) {
                throw ClassCastException("Actrivity $context must implement AppDialog.DialogEvents interface")
            }
        }
        //It is fragment but doesn't implement the interface
        //And if out attempt to cast the parent fragment fails, that means the fragment doesn't implement the interface
        catch (e: ClassCastException) {
            throw ClassCastException("Fragment $parentFragment must implement AppDialog.DialogEvents interface")
        }
    }

    //For a dialog fragment, we normally set everything up in the onCreateDialog function.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //fix "smart cast to Bundle is impossible, because 'arguments' is a mutable property that could have been change by this time

        val arguments = arguments
        val dialogId: Int
        val messageString: String?
        var positiveBtnID: Int
        var negativeBtnId: Int

        //As long there is a bundle, we can use those keys to retirve the values
        if (arguments != null) {
            //Our dialogFragment relies on being provided with at least message to display
            //and out callBachInterface, requires a callBack id
            dialogId = arguments.getInt(DIALOG_ID)
            messageString = arguments.getString(DIALOG_MESSAGE)

            if (dialogId == 0 || messageString == null) {
                throw IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE NOT present in the bundle")
            }
            //The text for the buttons is optional
            //If the text isn't provided for the two buttons, they'll default to showing OK or CANCEL
            positiveBtnID = arguments.getInt(DIALOG_POSITIVE_RID)
            negativeBtnId = arguments.getInt(DIALOG_NEGATIVE_RID)
            if (positiveBtnID == 0) {
                positiveBtnID = R.string.ok
            }
            if (negativeBtnId == 0) {
                negativeBtnId = R.string.cancel
            }
        }
        else {
            throw IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle")
        }


        val builder = AlertDialog.Builder(context)
        return builder.setMessage(messageString)
            .setPositiveButton(positiveBtnID, { dialog, id ->
                //the dialog will automatically dismiss itself when one of the buttons is tapped, so we don't have to worry about calling its dismiss function
                dialogEvents?.onPostiveDialogResult(dialogId, arguments)

            })
            .setNegativeButton(negativeBtnId, { dialog, id ->
                //the dialog will automatically dismiss itself when one of the buttons is tapped, so we don't have to worry about calling its dismiss function
            })
            .create();
    }


    //******************************************************* DETACH ****************************************************************//
    override fun onDetach() {
        Log.d(TAG, "onDetach called: context is $context")
        super.onDetach()
        //Reset the active callbacks interface, because we're no longer attached
        dialogEvents = null
    }

    //The cancel is the only way to respond when the dialog is cancelled, either by the user pressing the back buttons, or tapping the screen outside of the dialog
    //It indicates the user has removed the dialog, without using either oif the buttons
    override fun onCancel(dialog: DialogInterface) {
        Log.d(TAG,"onCancel called")
      val dialogId=arguments?.getInt(DIALOG_ID)
       // dialogEvents?.onDialogCancelled(dialogId!!)

    }

    override fun onDismiss(dialog: DialogInterface) {
        Log.d(TAG,"onDismiss called")
        super.onDismiss(dialog)  // <-- comment out this line, for strange results
    }

}
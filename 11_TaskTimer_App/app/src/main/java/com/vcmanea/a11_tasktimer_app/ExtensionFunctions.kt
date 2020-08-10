package com.vcmanea.a11_tasktimer_app

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.vcmanea.a11_tasktimer_app.dialogs.*


fun FragmentActivity.showConfirmationDialog(
    id: Int,
    message: String,
    positiveCaption: Int = R.string.ok,
    negativeCaption: Int = R.string.cancel
) {

    val args = Bundle().apply {
        putInt(DIALOG_ID, id)
        putString(DIALOG_MESSAGE, message)
        putInt(DIALOG_POSITIVE_RID, positiveCaption)
        putInt(DIALOG_NEGATIVE_RID, negativeCaption)
    }
    val dialog = AppDialog()
    dialog.arguments = args
    //support fragment manager because is called from an activity
    dialog.show(supportFragmentManager, null)


}
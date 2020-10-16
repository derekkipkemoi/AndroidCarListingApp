package org.carlistingapp.listingcarsproject.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar

fun View.snackBar(message: String?){
    Snackbar.make(this, message!!, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok"){
            snackbar.dismiss()
        }.show()
    }
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}
fun ProgressBar.hide(){
    visibility = View.GONE
}

fun ContentResolver.getFileName(uri: Uri): String {
    var name : String = ""
    val cursor = query(uri,null,null,null,null)
    cursor.use {
        it?.moveToFirst()
        name = cursor!!.getString(it!!.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}
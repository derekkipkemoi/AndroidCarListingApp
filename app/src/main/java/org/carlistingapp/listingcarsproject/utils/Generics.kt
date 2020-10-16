package org.carlistingapp.listingcarsproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import org.carlistingapp.listingcarsproject.R
import java.util.*

class NumberTextWatcherForThousand(editTextValue: EditText) : TextWatcher {

    private var editText = editTextValue
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
        try {
            editText.removeTextChangedListener(this)
            val value = editText.text.toString()
            if (value != "") {
                if (value.startsWith(".")) { //adds "0." when only "." is pressed on begining of writting
                    editText.setText("0.")
                }
                if (value.startsWith("0") && !value.startsWith("0.")) {
                    editText.setText("") //Prevents "0" while starting but not "0."
                }
                val str = editText.text.toString().replace(",".toRegex(), "")
                if (value != "") editText.setText(getDecimalFormattedString(str))
                editText.setSelection(editText.text.toString().length)
            }
            editText.addTextChangedListener(this)
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText.addTextChangedListener(this)
        }

    }

    private fun getDecimalFormattedString(value: String): String? {
        val lst = StringTokenizer(value, ".")
        var str1 = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = -1 + str1.length
        if (str1[-1 + str1.length] == '.') {
            j--
            str3 = "."
        }
        var k = j
        while (true) {
            if (k < 0) {
                if (str2.isNotEmpty()) str3 = "$str3.$str2"
                return str3
            }
            if (i == 3) {
                str3 = ",$str3"
                i = 0
            }
            str3 = str1[k].toString() + str3
            i++
            k--
        }
    }
}


class CustomAlertDialog(val activity: Activity) {
    private lateinit var alertDialog: AlertDialog
    @SuppressLint("InflateParams")
    fun startLoadingDialog(message : String){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.layout_alert_dialog,null)
        builder.setView(view)
        val textView: TextView = view.findViewById(R.id.textView)
        alertDialog = builder.create()
        textView.text = message
        alertDialog.show()
    }
    fun stopDialog(){
        alertDialog.dismiss()
    }
}

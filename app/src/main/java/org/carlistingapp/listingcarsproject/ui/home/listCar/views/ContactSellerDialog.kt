package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import org.carlistingapp.listingcarsproject.R


class ContactSellerDialog(val activity: Activity) {


    private lateinit var alertDialog: AlertDialog


    @SuppressLint("InflateParams", "SetTextI18n", "IntentReset")
    fun startLoadingContactDialog(phoneNumber: Int?, email: String?, carName: String?){

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.seller_contactdetails_layout, null)
        builder.setView(view)
        val textViewCarName: TextView = view.findViewById(R.id.car_name)
        val textViewPhone: TextView = view.findViewById(R.id.seller_phone)
        val textViewEmail: TextView = view.findViewById(R.id.seller_email)
        val buttonCall  = view.findViewById<ImageButton>(R.id.button_call)
        val buttonEmail  = view.findViewById<ImageButton>(R.id.button_email)
        val buttonMessage  = view.findViewById<ImageButton>(R.id.button_message)
        alertDialog = builder.create()
        textViewCarName.text = carName
        textViewPhone.text = "0" +phoneNumber.toString()
        textViewEmail.text = email
        buttonCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode("0" + phoneNumber.toString())))
            startActivity(this.activity, intent, null)
        }

        buttonMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:" + Uri.encode("0" + phoneNumber.toString()))
            intent.putExtra("sms_body", "Hallo, I am interested in your vehicle $carName Listed On Moti App")
            startActivity(this.activity, intent, null)
        }

        buttonEmail.setOnClickListener {
            if (email!!.isNotEmpty()){
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:" + Uri.encode(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, carName)
                intent.putExtra(Intent.EXTRA_TEXT, "Hallo, I am interested in your vehicle $carName Listed On Moti App")
                startActivity(this.activity, intent, null)
            }else{
                Snackbar.make(view, "Seller Did No Provide Email", Snackbar.LENGTH_SHORT).show()
            }


        }
        alertDialog.show()
    }


    fun stopContactDialog(){
        alertDialog.dismiss()
    }
}
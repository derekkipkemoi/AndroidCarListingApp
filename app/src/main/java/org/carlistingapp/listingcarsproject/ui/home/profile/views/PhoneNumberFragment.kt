package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidstudy.daraja.model.AccessToken
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.FragmentPhoneNumberBinding
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class PhoneNumberFragment : Fragment() {

    private lateinit var ccp : CountryCodePicker
    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var callBack : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var number: String
    private val PHONE_PATTERN  = Pattern.compile(
        //"^\\\\s*(?:\\\\+?(\\\\d{1,3}))?[-. (]*(\\\\d{3})[-. )]*(\\\\d{3})[-. ]*(\\\\d{4})(?: *x(\\\\d+))?\\\\s*\$"
        "\\d{10}"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false)
        val view = binding.root

        callBack = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential:  PhoneAuthCredential) {
            }

            override fun onVerificationFailed(onVerificationFailed: FirebaseException) {
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                binding.progressBar.visibility = View.INVISIBLE
                val bundle = bundleOf("verificationID" to verificationId,"phoneNumber" to number)
                findNavController().navigate(R.id.verifyPhoneCodeFragment, bundle)
            }
        }


        binding.buttonVerify.setOnClickListener {
            sendCode()
        }
        return view
    }

    private fun sendCode(){
        ccp = binding.ccp
        val countryCode = ccp.selectedCountryCodeWithPlus
        number = binding.textViewPhoneNumber.text?.trim().toString()


        binding.phoneTextInputLayout.error = null
        if (number.isEmpty()){
            binding.phoneTextInputLayout.error = "Phone Number Required"
            return
        }
        if (!PHONE_PATTERN.matcher(number).matches()){
            binding.phoneTextInputLayout.error = "Enter Correct Phone Number"
            return
        }

        val phoneNumber = countryCode.plus(number).trim()
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonVerify.isEnabled = false
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,requireActivity(),callBack)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
    }

}
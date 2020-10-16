package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.PhoneNumber
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentVerifyPhoneCodeBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.CustomAlertDialog
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class VerifyPhoneCodeFragment : Fragment() , KodeinAware {
    override val kodein by kodein()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory: UserViewModelFactory by instance()
    val session : SharedPrefManager by instance()
    private lateinit var viewModel: UserViewModel

    private lateinit var binding: FragmentVerifyPhoneCodeBinding
    private lateinit var verificationID: String
    private lateinit var phoneNumber : String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding = DataBindingUtil.inflate(inflater,R.layout.fragment_verify_phone_code, container, false)
        val view = binding.root
        verificationID = arguments?.getString("verificationID").toString()
        phoneNumber = arguments?.getString("phoneNumber").toString()
        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this.requireActivity(),factory).get(UserViewModel ::class.java)
        binding.buttonVerifyCode.setOnClickListener {
            verifySentCode()
        }


        return view
    }

    @SuppressLint("SetTextI18n")
    private fun verifySentCode(){
        val optView = binding.otpView
        val code = optView.text.toString().trim()

        if (code.isEmpty()){
            binding.warningText.visibility = View.VISIBLE
            binding.warningText.text = "Enter Verification Code Sent To Provided Number"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.warningText.visibility = View.INVISIBLE
        val credential = PhoneAuthProvider.getCredential(verificationID, code)

        signInWithPhoneAuthCredential(credential)
    }

    @SuppressLint("SetTextI18n")
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.buttonVerifyCode.isEnabled = false;
                    binding.successText.visibility = View.VISIBLE
                    binding.successText.text = "Code Verified Successfully"
                    val customAlertDialog = CustomAlertDialog(requireActivity())
                    customAlertDialog.startLoadingDialog("Updating User Phone Number")
                    val phoneNumberObject = PhoneNumber( phoneNumber.toInt())
                    try {
                    viewModel.verifyUserPhoneNumber(requireContext(),phoneNumberObject)
                    binding.progressBar.visibility = View.INVISIBLE
                        viewModel.userPhoneObject.observe(viewLifecycleOwner, Observer { userPhoneObject ->
                            customAlertDialog.stopDialog()
                            binding.root.snackBar(userPhoneObject.message)
                            if (userPhoneObject.userObject?.phoneNumber?.verified == true){
                                session.saveUserPhoneNumberVerified(true)
                                session.saveUserPhoneNumber(userPhoneObject.userObject.phoneNumber.number!!)
                                findNavController().popBackStack(R.id.phoneNumberFragment, true)
                                findNavController().popBackStack(R.id.verifyPhoneCodeFragment, true)
                                loadHomeActivity()
                            }
                        })
                    }catch (e : NoInternetException){
                        binding.root.snackBar(e.message)
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.buttonVerifyCode.isEnabled = true;
                        binding.warningText.visibility = View.VISIBLE
                        binding.warningText.text = "The verification code entered was invalid"
                    }
                }
            }
    }

    private fun loadHomeActivity() {
        findNavController().navigate(R.id.indexFragment)
    }
}
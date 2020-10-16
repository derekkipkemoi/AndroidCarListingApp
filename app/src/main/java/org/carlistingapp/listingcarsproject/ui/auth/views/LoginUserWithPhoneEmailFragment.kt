package org.carlistingapp.listingcarsproject.ui.auth.views

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentLoginUserPhoneEmailBinding
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModel
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModelFactory
import org.carlistingapp.listingcarsproject.utils.CustomAlertDialog
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import java.util.regex.Pattern


class LoginUserWithPhoneEmailFragment() : Fragment(), KodeinAware {
    override val kodein by kodein()
    val api : ListingCarsAPI by instance()
    val repository : AuthRepository by instance()
    val factory: AuthViewModelFactory by instance()
    val session : SharedPrefManager by instance()

    private lateinit var viewModel : AuthViewModel
    private lateinit var binding : FragmentLoginUserPhoneEmailBinding

    private val PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +  /
            // /at least 1 digit
            // "(?=.*[a-z])" +       //at least 1 lower case letter
            //"(?=.*[A-Z])" +       //at least 1 upper case letter
            //"(?=.*[a-zA-Z])" +    //any letter
            //"(?=.*[@#$%^&+=])" +  //at least 1 special character
            //"(?=\\S+$)" +         //no white spaces
            ".{4,}" +               //at least 4 characters
            "$"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_user_phone_email, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this.requireActivity(),factory).get(AuthViewModel::class.java)
        binding.buttonSignIn.setOnClickListener {
            signInUser()
        }

        binding.registerButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.registerUserPhoneEmailFragment)
        }

        return view
    }

    private fun signInUser() {
        val email = binding.textViewEmail.text.toString().trim()
        val password = binding.textViewPassword.text.toString().trim()

        binding.emailTextInputLayout.error = null
        binding.passwordTextInputLayout.error = null

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailTextInputLayout.error = "Valid Email Address Required!!"
            return
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()){
            binding.passwordTextInputLayout.error = "Password Should Have At Least 4 Characters!!"
            return
        }

        val customAlertDialog = CustomAlertDialog(requireActivity())
        customAlertDialog.startLoadingDialog("Logging In User")
        binding.progressBar.visibility = View.VISIBLE
        try {
        viewModel.getLoggedInUser(email,password)
            viewModel.loggedInUserResponse.observe(viewLifecycleOwner, Observer {loggedInUserResponse ->
                customAlertDialog.stopDialog()
                binding.progressBar.visibility = View.INVISIBLE
                if (loggedInUserResponse.userObject?.id != null){
                    binding.root.snackBar(loggedInUserResponse.message)
                    session.saveSession(loggedInUserResponse.userObject.id)
                    session.saveUserName(loggedInUserResponse.userObject.local?.name!!)
                    session.saveUserEmail(loggedInUserResponse.userObject.local.email!!)
                    session.saveUserPhoneNumber(loggedInUserResponse.userObject.phoneNumber?.number!!)
                    session.saveUserPhoneNumberVerified(loggedInUserResponse.userObject.phoneNumber.verified!!)
                    session.saveUserSignInMethod(loggedInUserResponse.userObject.method!!)
                    findNavController().popBackStack(R.id.signInUserFragment, true)
                    loadHomeActivity()
                }
                else{
                    binding.root.snackBar(loggedInUserResponse.message)
                }
            })
        }catch (e : NoInternetException){
            binding.root.snackBar(e.message)
        }
    }

    private fun loadHomeActivity() {
            findNavController().navigate(R.id.listCarFragment)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

    }
}


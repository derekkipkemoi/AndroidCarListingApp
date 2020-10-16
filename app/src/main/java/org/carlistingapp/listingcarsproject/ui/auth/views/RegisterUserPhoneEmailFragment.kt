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
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentRegisterUserPhoneEmailBinding
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModel
import org.carlistingapp.listingcarsproject.ui.auth.viewModel.AuthViewModelFactory
import org.carlistingapp.listingcarsproject.utils.CustomAlertDialog
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterUserPhoneEmailFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val session : SharedPrefManager by instance()
    private val api : ListingCarsAPI by instance()
    private val repository : AuthRepository by instance()
    private val factory : AuthViewModelFactory by instance()


    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterUserPhoneEmailBinding

    private val PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +  /
            // /at least 1 digit
            // "(?=.*[a-z])" +       //at least 1 lower case letter
            //"(?=.*[A-Z])" +       //at least 1 upper case letter
            //"(?=.*[a-zA-Z])" +    //any letter
            //"(?=.*[@#$%^&+=])" +  //at least 1 special character
            //"(?=\\S+$)" +         //no white spaces
            ".{6,}" +               //at least 4 characters
            "$"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_user_phone_email, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this.requireActivity(),factory).get(AuthViewModel::class.java)
        binding.buttonSignUp.setOnClickListener {
            singUpUser()
        }
        return view
    }

    private fun singUpUser() {
        val firstName = binding.textViewFirstName.text.toString().trim()
        val lastName = binding.textViewLastName.text.toString().trim()
        val email = binding.textViewEmail.text.toString().trim()
        val password = binding.textViewPassword.text.toString().trim()
        val confirmPassword= binding.textViewConfirmPassword.text.toString().trim()

        binding.firstNameTextInputLayout.error = null
        binding.lastNameTextInputLayout.error = null
        binding.emailTextInputLayout.error = null
        binding.passwordTextInputLayout.error = null
        binding.confirmPasswordTextInputLayout.error = null

        val regx = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}\$"
        val pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
        val firstNameMatcher: Matcher = pattern.matcher(firstName)
        val lastNameMatcher: Matcher = pattern.matcher(lastName)

        if (!firstNameMatcher.matches()) {
            binding.firstNameTextInputLayout.error = "Valid first Name Required!!"
            return
        }
        if (!lastNameMatcher.matches()) {
            binding.lastNameTextInputLayout.error = "Valid last Name Required!!"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailTextInputLayout.error = "Valid Email Address Required!!"
            return
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()){
            binding.passwordTextInputLayout.error = "Password Should Have At Least 6 Characters!!"
            return
        }

        if (confirmPassword != password){
            binding.confirmPasswordTextInputLayout.error = "Passwords Do Not Match!!"
            return
        }



        val name = firstName.plus(" ").plus(lastName)

        binding.buttonSignUp.isEnabled = false
        val customAlertDialog = CustomAlertDialog(requireActivity())
        customAlertDialog.startLoadingDialog("Registering User")
        binding.progressBar.visibility = View.VISIBLE
        try {
        viewModel.getSignedUpUser(email,password,name)
            viewModel.registeredUserResponse.observe(viewLifecycleOwner, Observer {registeredUserResponse ->
                customAlertDialog.stopDialog()
                binding.progressBar.visibility = View.INVISIBLE
                if (registeredUserResponse.access_token != null){
                    binding.root.snackBar(registeredUserResponse.message)
                    session.saveSession(registeredUserResponse.userObject?.id)
                    session.saveUserName(registeredUserResponse.userObject?.local?.name!!)
                    session.saveUserEmail(registeredUserResponse.userObject.local.email!!)
                    session.saveUserPhoneNumber(registeredUserResponse.userObject.phoneNumber?.number!!)
                    session.saveUserPhoneNumberVerified(registeredUserResponse.userObject.phoneNumber.verified!!)
                    session.saveUserSignInMethod(registeredUserResponse.userObject.method!!)
                    findNavController().popBackStack(R.id.loginUserWithPhoneEmailFragment, true)
                    findNavController().popBackStack(R.id.signInUserFragment, true)
                    loadVerifyPhoneActivity()
                }
                else{
                    binding.buttonSignUp.isEnabled = true
                    binding.root.snackBar(registeredUserResponse.message)
                }
            })
        }catch (e : NoInternetException){
            binding.root.snackBar(e.message)
        }
    }

    private fun loadVerifyPhoneActivity() {
        findNavController().navigate(R.id.phoneNumberFragment)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }

}
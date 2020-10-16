package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.NameUpdate
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentEditUserProfileDialogBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EditUserProfileFragment : DialogFragment(), KodeinAware{
    override val kodein by kodein()
    private val networkConnectionInterceptor : NetworkConnectionInterceptor by instance()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory: UserViewModelFactory by instance()
    private val userDetail : SharedPrefManager by instance()
    private lateinit var viewModel: UserViewModel
    private lateinit var binding : FragmentEditUserProfileDialogBinding
    private lateinit var userInitialName : String
    private lateinit var userMethod : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_edit_user_profile_dialog, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        userMethod = userDetail.getUserSignInMethod().toString()
        userInitialName = userDetail.getUserName().toString()
        binding.editTextName.text = Editable.Factory.getInstance().newEditable(userInitialName)
        binding.textViewUserEmail.text = userDetail.getUserEmail()

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonUpdate.setOnClickListener {
            updateUserProfile()
        }
        return view
    }

    private fun updateUserProfile() {
        binding.nameTextInputLayout.error = null
        val userName = binding.editTextName.text?.trim().toString()
        if (userMethod == "facebook" || userMethod == "google"){
            binding.nameTextInputLayout.error = "Your Name can't be changed. Either you signed In with facebook or google"
            return
        }
        if (userName == userInitialName || userName.isEmpty() || !userName.contains(" ")){
            binding.nameTextInputLayout.error = "Type new name if you want to update"
            return
        }
        val userId = userDetail.getSession()
        val name = NameUpdate(userName)
        if (userId != null) {
            binding.progressBar.visibility = View.VISIBLE
            try {
                viewModel.updateUserName(name,userId)
                viewModel.updateName.observe(viewLifecycleOwner, Observer { updateName ->
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.root.snackBar(updateName.message)
                    userDetail.saveUserName(userName)
                    dismiss()
                })
            }catch (e:NoInternetException){
                binding.root.snackBar(e.message)
            }

        }
    }
}
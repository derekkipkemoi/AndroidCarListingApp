package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.FragmentUserProfileBinding
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UserProfilePFragment : Fragment() , KodeinAware{

    private lateinit var binding: FragmentUserProfileBinding
    override val kodein by kodein()
    private val session : SharedPrefManager by instance()



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile, container, false)
        val view = binding.root
        //val viewPager2 = activity?.findViewById<ViewPager2>(R.id.viewPager)


        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.myAds.setOnClickListener {
            findNavController().navigate(R.id.userCarsFragment)
        }

        binding.postAd.setOnClickListener {
            findNavController().popBackStack(R.id.userProfilePFragment, true)
            findNavController().navigate(R.id.fragmentPostCar)
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun updateUserProfileUI() {
        binding.name.text = session.getUserName()
        binding.email.text = session.getUserEmail()
        binding.phoneNumberEdit.visibility = View.GONE

        if (!session.getUserPhoneNumberVerified()){
            binding.phoneNumber.text = "Verify Number"
            binding.phoneNumberEdit.visibility = View.VISIBLE
            binding.phoneNumberLayout.setOnClickListener {
                findNavController().navigate(R.id.phoneNumberFragment)
            }
        }
        else{
            binding.phoneNumber.text = "0".plus(session.getUserPhoneNumber().toString())
        }

        if (session.getUserSignInMethod() == "local"){
            binding.methodIcon.setBackgroundResource(R.drawable.ic_mail)
            binding.method.text = "Local"
        }

        if (session.getUserSignInMethod() == "facebook"){
            binding.methodIcon.setBackgroundResource(R.drawable.ic_facebook_icon)
            binding.method.text = "Facebook"
        }

        if (session.getUserSignInMethod() == "google"){
            binding.methodIcon.setBackgroundResource(R.drawable.ic_google_icon)
            binding.method.text = "Google"
        }



    }

    override fun onStart() {
        super.onStart()
        if (session.getSession() == "userLoggedOut") {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                R.id.signInUserFragment
            )
        } else {
            updateUserProfileUI()
        }

    }

    override fun onResume() {
        super.onResume()
        if (session.getSession() == "userLoggedOut") {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                R.id.signInUserFragment
            )
        } else {
            updateUserProfileUI()
        }
    }


}
package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.FragmentEditUserPhoneBinding
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EditUserPhoneFragment : DialogFragment(), KodeinAware {
    override val kodein by kodein()
    val session : SharedPrefManager by instance()

    private lateinit var binding : FragmentEditUserPhoneBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_user_phone, container, false)
        val view = binding.root

        binding.textViewUserPhoneNumber.text = "0"+session.getUserPhoneNumber().toString()
        binding.textViewPhoneVerified.text = "Phone Number Verified"

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonUpdate.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.phoneNumberFragment)
        }
        return  view
    }

}
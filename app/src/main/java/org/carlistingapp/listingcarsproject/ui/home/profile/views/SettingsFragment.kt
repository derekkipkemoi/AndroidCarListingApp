package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.FragmentSettingsBinding
import org.carlistingapp.listingcarsproject.ui.home.HomeActivity
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SettingsFragment : Fragment() , KodeinAware{
    override val kodein by kodein()
    val session : SharedPrefManager by instance()
    private lateinit var binding : FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        val view = binding.root
        requireActivity().toolBar.title = "Settings"

        binding.editAccount.setOnClickListener {
            editAccount()
        }

        binding.phoneNumber.setOnClickListener {
            if (!session.getUserPhoneNumberVerified()){
                findNavController().navigate(R.id.phoneNumberFragment)
            }
            else
            editPhoneNumber()
        }
        binding.carDealer.setOnClickListener {
            Toast.makeText(requireContext(),"Coming Soon", Toast.LENGTH_LONG).show()
        }
        binding.logout.setOnClickListener {
            val session: SharedPreferences = requireActivity().getSharedPreferences("privatePreferenceName", Context.MODE_PRIVATE)
            session.edit().clear().apply();
            requireActivity().finish()
            val intent = Intent(requireActivity(),HomeActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun editPhoneNumber() {
        val editUserPhone = EditUserPhoneFragment()
        editUserPhone.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
        editUserPhone.show(requireActivity().supportFragmentManager,"EditUserPhone")
    }

    private fun editAccount() {
        val editUserProfileFragment = EditUserProfileFragment()
        editUserProfileFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
        editUserProfileFragment.show(requireActivity().supportFragmentManager,"EditUserProfileFragment")
    }

    override fun onStart() {
        super.onStart()
        activity?.bottomAppBar?.performHide()
    }

    override fun onResume() {
        super.onResume()
        activity?.bottomAppBar?.performHide()
    }

}
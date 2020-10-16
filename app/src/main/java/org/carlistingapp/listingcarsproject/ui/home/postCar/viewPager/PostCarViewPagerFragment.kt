package org.carlistingapp.listingcarsproject.ui.home.postCar.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_post_car_view_pager.view.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.ui.home.postCar.views.PostCarDetailsFragment
import org.carlistingapp.listingcarsproject.ui.home.postCar.views.PostCarFeaturesFragment
import org.carlistingapp.listingcarsproject.ui.home.postCar.views.PostCarImagesFragment
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PostCarViewPagerFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val session : SharedPrefManager by instance()
    
    override fun onStart() {
        super.onStart()
        if (session.getSession() == "userLoggedOut") {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                R.id.signInUserFragment
            )
        }

        if (!session.getUserPhoneNumberVerified()){
                findNavController().navigate(R.id.phoneNumberFragment)
            }
    }

    override fun onResume() {
        super.onResume()
        if (session.getSession() == "userLoggedOut") {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                R.id.signInUserFragment
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_car_view_pager, container, false)

        requireActivity().toolBar.title = ""
        //Create fragmentList
        val fragmentList = arrayListOf<Fragment>(PostCarDetailsFragment(), PostCarFeaturesFragment(), PostCarImagesFragment())

        //Create Adapter
        val adapter = PostCarViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)

        //reference view
        val tabLayout = view.tab_layout
        val viewPager = view.viewPager
        val circleIndicator3 = view.indicator



        //set ViewPager adapter
        viewPager.adapter = adapter
        circleIndicator3.setViewPager(viewPager)

        //Disable ViewPager2 Swipe
        viewPager.isUserInputEnabled = false;

        //add TabLayouts
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy{tab, position ->
            //Disable TabLayout Click
            tab.view.isClickable = false;
            when (position) {
                0 -> tab.text = "Car Details"
                1 -> tab.text = "Car Features"
                2 -> tab.text = "Car Images"
            }
        }).attach()

        return view
    }


}
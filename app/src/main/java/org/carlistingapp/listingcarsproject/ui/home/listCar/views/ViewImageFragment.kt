package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_post_car_view_pager.view.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentViewImageBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.ViewImageAdapter

class ViewImageFragment : Fragment() {
    private lateinit var binding: FragmentViewImageBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var car: CarObject
    private lateinit var imageList : List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=  DataBindingUtil.inflate(inflater,R.layout.fragment_view_image, container, false )
        val view = binding.root
        val circleIndicator3 = view.indicator
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        requireActivity().toolBar.title = car.name
        imageList = car.images!!
        viewPager2 = binding.viewPagerImageSlider
        viewPager2.adapter = ViewImageAdapter(imageList,requireContext())
        circleIndicator3.setViewPager(viewPager2)

        val adLoader = AdLoader.Builder(requireActivity(), getString(R.string.ad_unit_id))
            .forUnifiedNativeAd { ad: UnifiedNativeAd ->
                // Show the ad.
                val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable( Color.parseColor("#ffffff"))).build()
                val template: TemplateView = binding.myTemplate
                template.setStyles(styles)
                template.setNativeAd(ad);
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build()).build()
        adLoader.loadAd(AdRequest.Builder().build())
        return view
    }

}
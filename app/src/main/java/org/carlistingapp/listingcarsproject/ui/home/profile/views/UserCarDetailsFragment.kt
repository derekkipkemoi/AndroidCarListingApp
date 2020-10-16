package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentUserCarDetailsBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.ListCarFeaturesAdapter
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class UserCarDetailsFragment : Fragment() {
    private lateinit var binding : FragmentUserCarDetailsBinding
    private lateinit var car : CarObject
    private val featuresList = ArrayList<String>()
    private val imageList = ArrayList<SlideModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_car_details, container, false)
        val view = binding.root
        requireActivity().toolBar.title = ""
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        imageList.clear()
        featuresList.clear()
        for (image in car.images!!){
            imageList.add(SlideModel(image,car.name))
        }
        for (feature in car.features!!){
            featuresList.add(feature)
        }

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.price.text = NumberFormat.getNumberInstance(Locale.US).format(car.price).toString()
        binding.location.text = car.location
        binding.condition.text = car.condition
        binding.transmission.text = car.transmission
        binding.mileage.text = car.mileage.toString()+" Km"
        binding.fuel.text = car.fuel
        binding.engine.text = car.engineSize.toString()+" CC"
        binding.make.text = car.make
        binding.model.text = car.model
        binding.year.text = car.year.toString()
        binding.body.text= car.body
        binding.duty.text = car.duty
        binding.color.text = car.color
        binding.interior.text = car.color
        binding.interior.text = car.interior
        binding.description.text = car.description

        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val bundle = bundleOf("carObject" to car)
                Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                    R.id.viewImageFragment,
                    bundle
                )
            }
        })

        binding.featuresRecyclerView.also {
            it.layoutManager = GridLayoutManager(requireContext(),3,
                GridLayoutManager.VERTICAL,false)
            it.setHasFixedSize(false)
            it.adapter = ListCarFeaturesAdapter(featuresList, this.requireActivity())
        }

        binding.buttonEdit.setOnClickListener {
            val bundle = bundleOf("carObject" to car)
            val editCarDialogFragment  = EditUserCarDialogFragment()
            editCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
            editCarDialogFragment.arguments = bundle
            editCarDialogFragment.show(requireActivity().supportFragmentManager,"EditCarDialogFragment")
        }

        binding.buttonDelete.setOnClickListener {
            val bundle = bundleOf("carObject" to car)
            val deleteCarDialogFragment = DeleteUserCarDialogFragment()
            deleteCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
            deleteCarDialogFragment.arguments = bundle
            deleteCarDialogFragment.show(requireActivity().supportFragmentManager,"DeleteCarDialogFragment")
        }

        binding.buttonFeature.setOnClickListener {
            val bundle = bundleOf("carObject" to car)
            Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.featureCarFragment, bundle)
        }

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
        return  view
    }

}
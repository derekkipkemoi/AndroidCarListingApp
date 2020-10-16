package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import org.carlistingapp.listingcarsproject.databinding.FragmentCarDetailsBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.ListCarFeaturesAdapter
import org.carlistingapp.listingcarsproject.utils.snackBar
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CarDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCarDetailsBinding
    private lateinit var car : CarObject
    private val featuresList = ArrayList<String>()
    private val imageList = ArrayList<SlideModel>()
    private val imageListView = ArrayList<String>()
    private  var email : String? = null
    private  var carName : String? = null
    private  var phoneNumber : Int? = null

    override fun onResume() {
        super.onResume()
        activity?.bottomAppBar?.performHide()
    }

    override fun onStart() {
        super.onStart()
        activity?.bottomAppBar?.performHide()
    }

    override fun onStop() {
        super.onStop()
        imageList.clear()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding=  DataBindingUtil.inflate(inflater, R.layout.fragment_car_details, container, false)
        val view = binding.root



        requireActivity().toolBar.title = ""
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        for (image in car.images!!){
            imageList.add(SlideModel(image, car.name, ScaleTypes.CENTER_CROP))
        }
        featuresList.clear()
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

        phoneNumber = car.seller?.sellerNumber
        email = car.seller?.sellerEmail
        carName = car.name

        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                val bundle = bundleOf("carObject" to car)
                Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
                    R.id.viewImageFragment,
                    bundle
                )
            }
        })

        binding.buttonCall.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + Uri.encode("0" + phoneNumber.toString()))
            )
            ContextCompat.startActivity(requireActivity(), intent, null)
        }
        binding.buttonEmail.setOnClickListener {
            if (email!!.isNotEmpty()){
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:" + Uri.encode(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, carName)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hallo, I am interested in your vehicle $carName Listed On Moti App"
                )
                ContextCompat.startActivity(requireActivity(), intent, null)
            }
            else{
                binding.root.snackBar("Seller Did No Provide Email")
            }

        }
        binding.buttonMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:" + Uri.encode("0" + phoneNumber.toString()))
            intent.putExtra(
                "sms_body",
                "Hallo, I am interested in your vehicle $carName Listed On Moti App"
            )
            ContextCompat.startActivity(requireActivity(), intent, null)
        }

        binding.featuresRecyclerView.also {
            it.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = ListCarFeaturesAdapter(featuresList, this.requireActivity())
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
        return view
    }


}



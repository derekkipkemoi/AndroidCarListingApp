package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.BuildConfig
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentListCarsBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.ListForeignUsedCarAdapter
import org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels.ListCarViewModel
import org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels.ListCarViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class ListCarFragment : Fragment(), ListForeignUsedCarAdapter.OnItemClickListener {
    private lateinit var binding: FragmentListCarsBinding
    private lateinit var carsList : ArrayList<CarObject>
    private val  mRecyclerViewItems = ArrayList<Any>()
    private lateinit var contactSellerDialog: ContactSellerDialog
    private val NUMBER_OF_ADS = 5
    private lateinit var adLoader: AdLoader
    private lateinit var adapter: ListForeignUsedCarAdapter
    private var mNativeAds: ArrayList<UnifiedNativeAd> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_cars, container, false)
        val view = binding.root
        mRecyclerViewItems.clear()
        mNativeAds.clear()
        carsList = arguments?.getParcelableArrayList<CarObject>("carsList") as ArrayList<CarObject>
        for (car in carsList){
            mRecyclerViewItems.add(car)
        }
        loadNativeAds()
        adapter = ListForeignUsedCarAdapter(mRecyclerViewItems, this.requireActivity(), this)
        val fragmentName = arguments?.getString("FragmentName")
        requireActivity().toolBar.title = fragmentName

        val recyclerView = binding.recyclerView
           recyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = adapter
            }

        return view
    }

    override fun onItemClick(position: Int) {
        val bundle = bundleOf("carObject" to mRecyclerViewItems[position])
        Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
            R.id.carDetailsFragment,
            bundle
        )
    }

    override fun contactSellerOnClick(position: Int) {
        var email :String = ""
        contactSellerDialog = ContactSellerDialog(requireActivity())
        val carObject = mRecyclerViewItems[position] as CarObject
        if (carObject.seller?.sellerEmail != null){
            email = carObject.seller.sellerEmail
        }
        contactSellerDialog.startLoadingContactDialog(carObject.seller?.sellerNumber, email, carObject.name)
    }

    override fun shareButtonClick(position: Int) {
        val carObject = mRecyclerViewItems[position] as CarObject
        Glide.with(requireContext())
            .asBitmap()
            .load(carObject.images?.get(0))
            .apply(RequestOptions().signature(ObjectKey("signature string")))
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val path = MediaStore.Images.Media.insertImage(requireActivity().contentResolver, resource, "IMG_" + Calendar.getInstance().time,null);
                    val imageUri = Uri.parse(path)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check Out "+carObject.name+" Listed On Moti Car Listing App https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID)
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    shareIntent.type = "*/*"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(shareIntent, "send"))
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun insertAdsInCarsList() {
        if (mNativeAds.isEmpty()) {
            return
        }
        val offset: Int = mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 2
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index += offset
        }
        adapter.notifyDataSetChanged()
    }

    private fun loadNativeAds() {
        val builder: AdLoader.Builder = AdLoader.Builder(requireActivity(), getString(R.string.ad_unit_id))
        adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader.isLoading) {
                    insertAdsInCarsList()
                }
            }.withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.")
                        if (!adLoader.isLoading) {
                            insertAdsInCarsList()
                        }
                    }
                }).build()
        adLoader.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS)
    }


}


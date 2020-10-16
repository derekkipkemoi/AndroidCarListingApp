package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.carlistingapp.listingcarsproject.BuildConfig
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentFilterCarsBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.FilterCarsAdapter
import java.util.*

class FilterCarsFragment : Fragment() ,FilterCarsAdapter.OnItemClickListener{
    private lateinit var binding: FragmentFilterCarsBinding
    private  lateinit var carsList : ArrayList<CarObject>
    private lateinit var contactSellerDialog : ContactSellerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_cars, container, false)
        val view = binding.root
        carsList = arguments?.getParcelableArrayList<CarObject>("filteredCarList")!!

        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = FilterCarsAdapter(carsList, this.requireActivity(), this)
        }
        return view
    }

    override fun onItemClick(position: Int) {
        val bundle = bundleOf("carObject" to carsList[position])
        Navigation.findNavController(requireActivity(), R.id.fragment).navigate(
            R.id.carDetailsFragment,
            bundle
        )
    }

    override fun contactSellerOnClick(position: Int) {
        contactSellerDialog = ContactSellerDialog(requireActivity())
        val phoneNumber = carsList[position].seller?.sellerNumber
        val email = carsList[position].seller?.sellerEmail
        val carName = carsList[position].name
        contactSellerDialog.startLoadingContactDialog(phoneNumber, email, carName)
    }

    override fun shareButtonOnClick(position: Int) {
        Glide.with(requireActivity())
            .asBitmap()
            .load(carsList[position].images?.get(0))
            .apply(RequestOptions().signature(ObjectKey("signature string")))
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val path = MediaStore.Images.Media.insertImage(requireActivity().contentResolver, resource, "IMG_" + Calendar.getInstance().time,null);
                    val imageUri = Uri.parse(path)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check Out "+carsList[position].name+" Listed On Moti Car Listing App https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID)
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    shareIntent.type = "image/*"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(shareIntent, "send"))
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}
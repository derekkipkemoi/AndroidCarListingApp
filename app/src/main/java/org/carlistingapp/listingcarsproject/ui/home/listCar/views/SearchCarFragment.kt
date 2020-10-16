package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.fragment_search_car.*
import org.carlistingapp.listingcarsproject.BuildConfig
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentSearchCarBinding
import org.carlistingapp.listingcarsproject.ui.home.listCar.adapters.SearchCarsAdapter
import org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels.ListCarViewModel
import org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels.ListCarViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.collections.ArrayList

class SearchCarFragment : Fragment(),KodeinAware, SearchCarsAdapter.OnItemClickListener {
    override val kodein by kodein()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory : ListCarViewModelFactory by instance()
    private lateinit var viewModel : ListCarViewModel
    private lateinit var binding: FragmentSearchCarBinding
    private var carsList = ArrayList<CarObject>()
    private var path : String = ""
    private lateinit var adapter: SearchCarsAdapter
    private lateinit var contactSellerDialog: ContactSellerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_car, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this,factory).get(ListCarViewModel::class.java)
        binding.progressBar.visibility = View.VISIBLE

        viewModel.getCars()
        viewModel.cars.observe(viewLifecycleOwner, Observer { cars->
            binding.progressBar.visibility = View.INVISIBLE
            carsList = cars
            adapter = SearchCarsAdapter(cars, this.requireActivity(), this)
            recycler_view.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = adapter
            }
        })

        binding.filterFab.setOnClickListener {
            val bundle = bundleOf("carsList" to carsList)
            val filterCarDialogFragment = FilterCarDialogFragment()
            filterCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
            filterCarDialogFragment.arguments = bundle
            filterCarDialogFragment.show(requireActivity().supportFragmentManager, "filterCarDialogFragment")
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Moti"
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }
        })
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


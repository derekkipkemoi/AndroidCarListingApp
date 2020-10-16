package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_user_cars.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentUserCarsBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.adapter.UserCarsAdapter
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class UserCarsFragment : Fragment(), KodeinAware, UserCarsAdapter.OnItemClickListener {
    override val kodein by kodein()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory: UserViewModelFactory by instance()
    val session : SharedPrefManager by instance()
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUserCarsBinding



    private lateinit var carsList : List<CarObject>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (session.getSession() != "userLoggedOut"){
            val userID = session.getSession().toString()
            viewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)
            try {
                viewModel.getUserCars(userID)
                binding.progressBar.visibility = View.VISIBLE
                viewModel.userCars.observe(viewLifecycleOwner, Observer {userCars ->
                    binding.progressBar.visibility = View.INVISIBLE
                    recycler_view.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.setHasFixedSize(true)
                        it.adapter = UserCarsAdapter(userCars,this.requireActivity(),this)
                        carsList = userCars
                        if (userCars.isEmpty()){
                            binding.postAd.visibility = View.VISIBLE
                        }
                    }
                })
            }catch (e:NoInternetException){
                binding.root.snackBar(e.message)
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_cars, container, false)
        val view = binding.root
        binding.postAd.setOnClickListener {
            findNavController().popBackStack(R.id.userProfilePFragment, true)
            findNavController().popBackStack(R.id.userCarsFragment, true)
            findNavController().navigate(R.id.fragmentPostCar)
        }
        return view
    }

    override fun onItemClick(position: Int) {
        val bundle = bundleOf("carObject" to carsList[position])
        Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.userCarDetailsFragment, bundle)
    }


    override fun onClickEditUserCar(position: Int) {
        val bundle = bundleOf("carObject" to carsList[position])
        val editCarDialogFragment  = EditUserCarDialogFragment()
        editCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
        editCarDialogFragment.arguments = bundle
        editCarDialogFragment.show(requireActivity().supportFragmentManager,"EditCarDialogFragment")
    }

    override fun onClickDeleteUserCar(position: Int) {
        val bundle = bundleOf("carObject" to carsList[position])
        val deleteCarDialogFragment = DeleteUserCarDialogFragment()
        deleteCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
        deleteCarDialogFragment.arguments = bundle
        deleteCarDialogFragment.show(requireActivity().supportFragmentManager,"DeleteCarDialogFragment")
    }

    override fun onFeatureUserCar(position: Int) {
        val bundle = bundleOf("carObject" to carsList[position])
        Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.featureCarFragment, bundle)
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
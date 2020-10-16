package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentDeleteUserCarDialogBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DeleteUserCarDialogFragment : DialogFragment(), KodeinAware {
    override val kodein by kodein()
    private val networkConnectionInterceptor : NetworkConnectionInterceptor by instance()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel


    private lateinit var car : CarObject
    private lateinit var carId : String
    private lateinit var binding : FragmentDeleteUserCarDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_delete_user_car_dialog, container, false)
        val view = binding.root
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        carId = car._id.toString()

        binding.textViewCarName.text = car.name
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonDelete.setOnClickListener {
            deleteCar()
        }
        return  view
    }

    private fun deleteCar() {
        val delete = binding.textViewCarDelete.text?.trim().toString()
        binding.deleteTextInputLayout.error = null
        if (delete.isEmpty() || delete != "DELETE"){
            binding.deleteTextInputLayout.error = "Type DELETE in Capital Letters"
            return
        }

        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        try {
        viewModel.deleteUserCar(carId)
        binding.progressBar.visibility = View.VISIBLE
            viewModel.deleteUserCar.observe(viewLifecycleOwner, Observer { deleteUserCar ->
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireActivity(), deleteUserCar.message,Toast.LENGTH_LONG).show()
                findNavController().popBackStack(R.id.userCarsFragment, true)
                findNavController().navigate(R.id.userCarsFragment)
                dismiss()
            })
        }catch (e : NoInternetException){
            binding.root.snackBar(e.message)
        }
    }

}
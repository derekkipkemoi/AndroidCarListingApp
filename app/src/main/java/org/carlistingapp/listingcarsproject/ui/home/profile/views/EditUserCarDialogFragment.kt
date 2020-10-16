package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.db.entities.CarUpdate
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentEditUserCarBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.carlistingapp.listingcarsproject.utils.NumberTextWatcherForThousand
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.NumberFormat
import java.util.*


class EditUserCarDialogFragment : DialogFragment(), KodeinAware {
    override val kodein by kodein()
    private val networkConnectionInterceptor : NetworkConnectionInterceptor by instance()
    val api : ListingCarsAPI by instance()
    val repository : UserRepository by instance()
    val factory: UserViewModelFactory by instance()


    private lateinit var binding : FragmentEditUserCarBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var selectedCarCondition : String
    private lateinit var selectedCarDuty : String
    private lateinit var selectedCarLocation : String
    private lateinit var car : CarObject
    private lateinit var carInitialCondition : String
    private lateinit var carInitialDuty : String
    private lateinit var carInitialLocation : String
    private lateinit var carInitialMileage : String
    private lateinit var carInitialDescription : String
    private lateinit var carInitialPrice : String
    private lateinit var carId : String
    private var carInitialPriceNegotiable : Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_user_car,
            container,
            false
        )
        val view = binding.root

        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        val carConditionSpinner = binding.carConditionSpinner
        val carDutySpinner = binding.carDutySpinner
        val carLocationSpinner = binding.carLocationSpinner
        binding.textViewCarPrice.addTextChangedListener(NumberTextWatcherForThousand(binding.textViewCarPrice))
        binding.textViewCarMileage.addTextChangedListener(NumberTextWatcherForThousand(binding.textViewCarMileage))
        carId = car._id.toString()
        carInitialCondition = car.condition.toString()

        carInitialDuty = car.duty.toString()
        carInitialLocation = car.location.toString()
        carInitialDescription = car.description.toString()
        carInitialMileage = NumberFormat.getNumberInstance(Locale.US).format(car.mileage).toString()
        carInitialPrice = NumberFormat.getNumberInstance(Locale.US).format(car.price).toString()
        carInitialPriceNegotiable = car.priceNegotiable

        binding.carName.text = car.name
        binding.textViewCarDescription.text = Editable.Factory.getInstance().newEditable(
            carInitialDescription
        )
        binding.textViewCarMileage.text = Editable.Factory.getInstance().newEditable(
            carInitialMileage
        )
        binding.textViewCarPrice.text = Editable.Factory.getInstance().newEditable(carInitialPrice)



        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.cars_condition_array,
            R.layout.style_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.style_spinner_dropdown_list)
            carConditionSpinner.adapter = adapter
            carConditionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCarCondition = if (position == 0){
                        carInitialCondition
                    }else
                        parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCarCondition = carInitialCondition
                }
            }
        }


        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.cars_duty_array,
            R.layout.style_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.style_spinner_dropdown_list)
            carDutySpinner.adapter = adapter
            carDutySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCarDuty = if (position == 0){
                        carInitialDuty
                    }else
                        parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCarDuty = carInitialDuty
                }
            }
        }

        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.cars_location_array,
            R.layout.style_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.style_spinner_dropdown_list)
            carLocationSpinner.adapter = adapter
            carLocationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCarLocation = if (position == 0){
                        carInitialLocation
                    }else
                        parent?.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCarLocation = carInitialLocation
                }
            }
        }


        binding.buttonUpdate.setOnClickListener {
            updateCar()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        return view
    }

    private fun updateCar() {
        binding.mileageTextInputLayout.error = null
        binding.priceTextInputLayout.error = null
        val carDescription = binding.textViewCarDescription.text.toString().trim()
        val carMileage = binding.textViewCarMileage.text.toString().trim()
        val carPrice = binding.textViewCarPrice.text.toString().trim()
        val carPriceNegotiable = binding.priceNegotiableCheckBox.isChecked
       if (carMileage == carInitialMileage
           && carDescription == carInitialDescription
           && carPrice == carInitialPrice
           && carPriceNegotiable == carInitialPriceNegotiable){
           binding.root.snackBar("You must edit at least one field in order to update")
           return
       }


        if (carMileage.isEmpty()){
            binding.mileageTextInputLayout.error = "Mileage Required"
            return
        }

        if (carPrice.isEmpty()){
            binding.priceTextInputLayout.error = "Price Required"
            return
        }

        val carUpdate = CarUpdate(
            selectedCarCondition,
            selectedCarDuty,
            selectedCarLocation,
            trimCommaOfString(carMileage).toInt(),
            carDescription,
            trimCommaOfString(carPrice).toInt(),
            carPriceNegotiable
        )

        //val convert = GsonBuilder().create()
        //val carJsonObject  = convert.toJson(userCarObject)
        try {
        viewModel.updateUserCar(carUpdate, carId)
        binding.progressBar.visibility = View.VISIBLE
            viewModel.getUpdateUserCar.observe(viewLifecycleOwner, Observer { getUpdateUserCar ->
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireActivity(), getUpdateUserCar.name +" Updated Successfully", Toast.LENGTH_LONG).show()
                findNavController().popBackStack(R.id.userCarsFragment, true)
                findNavController().navigate(R.id.userCarsFragment)
                dismiss()
            })
        }catch (e : NoInternetException){
            binding.root.snackBar(e.message)
        }
    }

    private fun trimCommaOfString(string: String): String {
        return if (string.contains(",")) {
            string.replace(",", "")
        } else {
            string
        }
    }

}
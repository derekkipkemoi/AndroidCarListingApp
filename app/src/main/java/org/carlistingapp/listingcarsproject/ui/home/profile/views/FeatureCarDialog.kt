package org.carlistingapp.listingcarsproject.ui.home.profile.views

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.TransactionType
import com.hbb20.CountryCodePicker
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.db.entities.FeatureUserCar
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.NetworkConnectionInterceptor
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentFeatureCarDialogBinding
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModel
import org.carlistingapp.listingcarsproject.ui.home.profile.viewModel.UserViewModelFactory
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.regex.Pattern


class FeatureCarDialog : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val networkConnectionInterceptor : NetworkConnectionInterceptor by instance()
    private val api : ListingCarsAPI by instance()
    private val repository : UserRepository by instance()
    private val factory: UserViewModelFactory by instance()
    private lateinit var viewModel: UserViewModel


    private lateinit var ccp : CountryCodePicker
    private lateinit var packageAmount : String
    private lateinit var packageName : String
    private lateinit var daraja : Daraja
    private val PHONE_PATTERN  = Pattern.compile(
        //"^\\\\s*(?:\\\\+?(\\\\d{1,3}))?[-. (]*(\\\\d{3})[-. )]*(\\\\d{3})[-. ]*(\\\\d{4})(?: *x(\\\\d+))?\\\\s*\$"
        "\\d{10}"
    )
    private lateinit var binding: FragmentFeatureCarDialogBinding
    private lateinit var car : CarObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this.requireActivity(),factory).get(UserViewModel::class.java)
        daraja = Daraja.with(
            "5MloVpYwhwqTEg5qdVUvygVkTS6UVbxB",
            "NkcKip7mOUrq2n87",
            object : DarajaListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {
                    Log.d(TAG, accessToken.access_token)
                }

                override fun onError(error: String) {
                    Log.d(TAG, error)
                }
            })
    }



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_feature_car_dialog,
            container,
            false
        )
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        packageAmount = arguments?.getString("Package").toString()
        packageName = arguments?.getString("Name").toString()
        binding.textViewCarName.text = car.name
        binding.packageName.text = "$packageName Payment"

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonConfirm.setOnClickListener {
            confirmCarBoost()
        }

        return binding.root
    }

    private fun confirmCarBoost() {

        ccp = binding.ccp
        val countryCode = ccp.selectedCountryCode
        val number = binding.textViewPhoneNumber.text?.trim().toString()


        binding.phoneTextInputLayout.error = null
        if (number.isEmpty()){
            binding.phoneTextInputLayout.error = "Phone Number Required"
            return
        }
        if (!PHONE_PATTERN.matcher(number).matches()){
            binding.phoneTextInputLayout.error = "Enter Correct Phone Number"
            return
        }


        val phoneNumber = countryCode.plus(removeFirstChar(number)).trim()
        binding.progressBar.visibility = View.VISIBLE



        val lnmExpress = LNMExpress(
            "174379",
            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
            TransactionType.CustomerBuyGoodsOnline,
            packageAmount,
            phoneNumber,
            "174379",
            phoneNumber,
            "https://165c1d661543.ngrok.io/users/mpesa",
            "001ABC",
            "Goods Payment"
        )

        daraja.requestMPESAExpress(lnmExpress,
            object : DarajaListener<LNMResult> {
                override fun onResult(lnmResult: LNMResult) {
                    Log.d(TAG, lnmResult.ResponseDescription)
                    binding.root.snackBar(lnmResult.ResponseDescription)
                    dismiss()
                    val featureUserCar = FeatureUserCar(packageName, packageAmount)
                    viewModel.featureCar(car._id!!, featureUserCar)
                    viewModel.featureCar.observe(viewLifecycleOwner, Observer { featureCar ->
                        binding.root.snackBar(featureCar.message)
                    })
                }

                override fun onError(error: String) {
                    Log.d(TAG, error)
                    binding.root.snackBar(error)
                }
            }
        )
    }

    private fun removeFirstChar(s: String): String? {
        return s.substring(1)
    }


}
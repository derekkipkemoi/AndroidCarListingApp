package org.carlistingapp.listingcarsproject.ui.home.postCar.views

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.databinding.FragmentPostCarFeaturesBinding
import org.carlistingapp.listingcarsproject.utils.NumberTextWatcherForThousand
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager
import org.carlistingapp.listingcarsproject.utils.snackBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.properties.Delegates


class PostCarFeaturesFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val carFeatures : SharedPrefManager by instance()
    private lateinit var binding: FragmentPostCarFeaturesBinding
    private lateinit var carSelectedFuel : String
    private lateinit var carSelectedInterior : String
    private lateinit var carSelectedColor : String
    private lateinit var carSelectedLocation : String
    private var checkedCarFuel by Delegates.notNull<Int>()
    private var checkedCarInterior by Delegates.notNull<Int>()
    private var checkedCarColor by Delegates.notNull<Int>()
    private var checkedCarLocation by Delegates.notNull<Int>()
    private val commonFeaturesList = HashSet<String>()
    private val commonFeaturesCheckedStates = booleanArrayOf(false, false, false, false, false, false, false, false, false, false, false, false, false)
    private val extraFeaturesCheckedStates = booleanArrayOf(false, false, false, false, false, false, false, false, false, false, false, false, false)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_car_features, container, false)
        val view = binding.root
        checkedCarFuel = -1
        checkedCarInterior = -1
        checkedCarColor = -1
        checkedCarLocation = -1
        binding.textViewCarEngineSize.addTextChangedListener(NumberTextWatcherForThousand(binding.textViewCarEngineSize))

        binding.carFuelButton.setOnClickListener {
            selectCarFuel()
        }

        binding.carInteriorButton.setOnClickListener {
            selectCarInterior()
        }

        binding.carColorButton.setOnClickListener {
            selectCarColor()
        }

        binding.carLocationButton.setOnClickListener {
            selectCarLocation()
        }


        binding.commonFeaturesButton.setOnClickListener {
            commonFeaturesDialog()
        }

        binding.extraFeaturesButton.setOnClickListener {
            extraFeaturesDialog()
        }

        binding.buttonBack.setOnClickListener {
            val viewPager2 = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager2?.currentItem = 0
        }

        binding.buttonNext.setOnClickListener {
            postCarFeatures()
        }
        return view
    }

    private fun selectCarFuel() {
        val carFuelDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carFuelList = resources.getStringArray(R.array.cars_fuel_array)
        val title = SpannableString("SELECT CAR FUEL")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carFuelDialog.setTitle(title)
        carFuelDialog.setSingleChoiceItems(carFuelList, checkedCarFuel) { _, which ->
            checkedCarFuel = which
            binding.carFuelButton.text = carFuelList[which]
            carSelectedFuel = carFuelList[which].toString()
        }
        carFuelDialog.setPositiveButton("Ok") { _, _ ->
        }
        carFuelDialog.setNegativeButton("Cancel", null)
        val dialog = carFuelDialog.create()
        dialog.show()
    }

    private fun selectCarInterior() {
        val carInteriorDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carInteriorList = resources.getStringArray(R.array.cars_interior_array)
        val title = SpannableString("SELECT CAR INTERIOR")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carInteriorDialog.setTitle(title)
        carInteriorDialog.setSingleChoiceItems(carInteriorList, checkedCarInterior) { _, which ->
            checkedCarInterior = which
            binding.carInteriorButton.text = carInteriorList[which]
            carSelectedInterior = carInteriorList[which].toString()
        }
        carInteriorDialog.setPositiveButton("Ok") { _, _ ->
        }
        carInteriorDialog.setNegativeButton("Cancel", null)
        val dialog = carInteriorDialog.create()
        dialog.show()
    }

    private fun selectCarColor() {
        val carColorDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carColorList = resources.getStringArray(R.array.cars_color_array)
        val title = SpannableString("SELECT CAR COLOR")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carColorDialog.setTitle(title)
        carColorDialog.setSingleChoiceItems(carColorList, checkedCarColor) { _, which ->
            checkedCarColor = which
            binding.carColorButton.text = carColorList[which]
            carSelectedColor = carColorList[which].toString()
        }
        carColorDialog.setPositiveButton("Ok") { _, _ ->
        }
        carColorDialog.setNegativeButton("Cancel", null)
        val dialog = carColorDialog.create()
        dialog.show()
    }

    private fun selectCarLocation() {
        val carLocationDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carLocationList = resources.getStringArray(R.array.cars_location_array)
        val title = SpannableString("SELECT CAR LOCATION")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carLocationDialog.setTitle(title)
        carLocationDialog.setSingleChoiceItems(carLocationList, checkedCarLocation) { _, which ->
            checkedCarLocation = which
            binding.carLocationButton.text = carLocationList[which]
            carSelectedLocation = carLocationList[which].toString()
        }
        carLocationDialog.setPositiveButton("Ok") { _, _ ->
        }
        carLocationDialog.setNegativeButton("Cancel", null)
        val dialog = carLocationDialog.create()
        dialog.show()
    }

    private fun commonFeaturesDialog() {
         val commonFeaturesAlertDialog = AlertDialog.Builder(this.requireContext())
         val commonFeaturesChecked = resources.getStringArray(R.array.car_common_features_array)
        val title = SpannableString("COMMON FEATURES")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        commonFeaturesAlertDialog.setTitle(title)
        commonFeaturesAlertDialog.setMultiChoiceItems(R.array.car_common_features_array,commonFeaturesCheckedStates) { _: DialogInterface, which: Int, isChecked: Boolean ->
            if (isChecked){
                commonFeaturesCheckedStates[which] = isChecked
                commonFeaturesList.add(commonFeaturesChecked[which])
            }
            else if (commonFeaturesList.contains(commonFeaturesChecked[which])){
                commonFeaturesList.remove(commonFeaturesChecked[which])
            }
        }
        commonFeaturesAlertDialog.setPositiveButton("ADD FEATURES") { _, _ ->
            Snackbar.make(this.requireView(),"$commonFeaturesList Added to Features",Snackbar.LENGTH_LONG).show()
        }
        commonFeaturesAlertDialog.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }
        commonFeaturesAlertDialog.create()
        commonFeaturesAlertDialog.show()
    }


    private fun extraFeaturesDialog() {
        val extraFeaturesAlertDialog = AlertDialog.Builder(this.requireContext())
        val extraFeaturesChecked = resources.getStringArray(R.array.car_extra_features_array)
        val title = SpannableString("EXTRA FEATURES")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        extraFeaturesAlertDialog.setTitle(title)
        extraFeaturesAlertDialog.setMultiChoiceItems(R.array.car_extra_features_array,extraFeaturesCheckedStates) { _: DialogInterface, which: Int, isChecked: Boolean ->
            if (isChecked){
                extraFeaturesCheckedStates[which] = isChecked
                commonFeaturesList.add(extraFeaturesChecked[which])
            }
            else if (commonFeaturesList.contains(extraFeaturesChecked[which])){
                commonFeaturesList.remove(extraFeaturesChecked[which])
            }
        }
        extraFeaturesAlertDialog.setPositiveButton("ADD FEATURES") { _, _ ->
            Snackbar.make(this.requireView(),"$commonFeaturesList Added to Features",Snackbar.LENGTH_LONG).show()
        }
        extraFeaturesAlertDialog.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }
        extraFeaturesAlertDialog.create()
        extraFeaturesAlertDialog.show()
    }

    private fun postCarFeatures() {
        val viewPager2 = activity?.findViewById<ViewPager2>(R.id.viewPager)
        val carEngineSize = binding.textViewCarEngineSize.text.toString().trim()
        val  carDescription = binding.textViewCarDescription.text.toString().trim()

        binding.carEngineSizeTextInputLayout.error = null
        binding.carDescriptionTextInputLayout.error = null

        if (checkedCarFuel == -1){
            binding.root.snackBar("Please Select Car Fuel Type")
            return
        }

        if (checkedCarInterior == -1){
            binding.root.snackBar("Please Select Car Interior Type")
            return
        }

        if (checkedCarColor == -1){
            binding.root.snackBar("Please Select Car Color Type")
            return
        }

        if (checkedCarLocation == -1){
            binding.root.snackBar("Please Select County Where Vehicle is Located")
            return
        }

        if (carEngineSize.isEmpty()) {
            binding.carEngineSizeTextInputLayout.error = "Engine Size Required"
            return
        }
        if (carDescription.length <= 10){
            binding.carDescriptionTextInputLayout.error = "Description too Short"
            return
        }

        carFeatures.saveCarFuel(carSelectedFuel)
        carFeatures.saveCarInterior(carSelectedInterior)
        carFeatures.saveCarColor(carSelectedColor)
        carFeatures.saveCarEngineSize(trimCommaOfString(carEngineSize).toInt())
        carFeatures.saveCarDescription(carDescription)
        carFeatures.saveCarCommonFeatures(commonFeaturesList)
        carFeatures.saveCarLocation(carSelectedLocation)
        viewPager2?.currentItem = 2

    }

    private fun trimCommaOfString(string: String): String {
        return if (string.contains(",")) {
            string.replace(",", "")
        } else {
            string
        }
    }

}
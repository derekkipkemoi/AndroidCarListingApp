package org.carlistingapp.listingcarsproject.ui.home.listCar.views

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentFilterCarDialogBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class FilterCarDialogFragment : DialogFragment() {
    private lateinit var carMakeList: Array<String?>
    private lateinit var carModelList: Array<String?>
    private lateinit var carMakeSelected: String
    private lateinit var carModelSelected: String
    private lateinit var carYearSelected: String
    private lateinit var carBodyTypeSelected: String
    private lateinit var carConditionSelected: String
    private var checkedCarMake by Delegates.notNull<Int>()
    private var checkedCarModel by Delegates.notNull<Int>()
    private var checkedCarYear by Delegates.notNull<Int>()
    private var checkedCarBody by Delegates.notNull<Int>()
    private var checkedCarCondition by Delegates.notNull<Int>()
    private lateinit var binding: FragmentFilterCarDialogBinding
    private lateinit var carsList: ArrayList<CarObject>
    private lateinit var filteredCarList: List<CarObject>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_car_dialog, container, false)
        val view = binding.root
        checkedCarMake = -1
        checkedCarModel = -1
        checkedCarYear = -1
        checkedCarBody = -1
        checkedCarCondition = -1
        binding.carModelButton.isEnabled = false

        carsList = arguments?.getParcelableArrayList<CarObject>("carsList")!!
        filteredCarList = carsList

        binding.carMakeButton.setOnClickListener {
            selectCarMake()
        }

        binding.carModelButton.setOnClickListener {
            selectCarModel()
        }

        binding.carYearButton.setOnClickListener {
            selectCarYear()
        }

        binding.carBodyButton.setOnClickListener {
            selectCarBody()
        }

        binding.carConditionButton.setOnClickListener {
            selectCarCondition()
        }

        binding.buttonFilter.setOnClickListener {
            filterCarsList()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        return view
    }




    private fun selectCarMake() {
        carMakeList = resources.getStringArray(R.array.cars_array)
        val carMakeListDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val title = SpannableString("SELECT CAR MAKE")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carMakeListDialog.setTitle(title)
        carMakeListDialog.setSingleChoiceItems(carMakeList, checkedCarMake) { _, which ->
            when (which) {
                0 -> {
                    carModelList = resources.getStringArray(R.array.cars_toyota_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                1 -> {
                    carModelList = resources.getStringArray(R.array.cars_nissan_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                2 -> {
                    carModelList = resources.getStringArray(R.array.car_subaru_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                3 -> {
                    carModelList = resources.getStringArray(R.array.car_honda_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                4 -> {
                    carModelList = resources.getStringArray(R.array.car_mitsubishi_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                5 -> {
                    carModelList = resources.getStringArray(R.array.car_mercedes_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                6 -> {
                    carModelList = resources.getStringArray(R.array.car_mazda_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                7 -> {
                    carModelList = resources.getStringArray(R.array.car_volkwagen_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                8 -> {
                    carModelList = resources.getStringArray(R.array.car_bmw_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                9 -> {
                    carModelList = resources.getStringArray(R.array.car_land_rover_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                10 -> {
                    carModelList = resources.getStringArray(R.array.car_isuzu_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                11 -> {
                    carModelList = resources.getStringArray(R.array.car_audi_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                12 -> {
                    carModelList = resources.getStringArray(R.array.car_suzuki_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                13 -> {
                    carModelList = resources.getStringArray(R.array.car_lexus_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                14 -> {
                    carModelList = resources.getStringArray(R.array.car_ford_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                15 -> {
                    carModelList = resources.getStringArray(R.array.car_alfaromeo_array)
                    checkedCarMake = which
                    checkedCarModel = -1
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                16 -> {
                    carModelList = resources.getStringArray(R.array.car_audi_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                17 -> {
                    carModelList = resources.getStringArray(R.array.motorbike_bajaj_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                18 -> {
                    carModelList = resources.getStringArray(R.array.car_bmw_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                19 -> {
                    carModelList = resources.getStringArray(R.array.car_cadillac_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                20 -> {
                    carModelList = resources.getStringArray(R.array.car_caterpillar_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                21 -> {
                    carModelList = resources.getStringArray(R.array.car_chery_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                22 -> {
                    carModelList = resources.getStringArray(R.array.car_daf_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                23 -> {
                    carModelList = resources.getStringArray(R.array.car_daihatsu_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                24 -> {
                    carModelList = resources.getStringArray(R.array.car_faw_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                25 -> {
                    carModelList = resources.getStringArray(R.array.car_ford_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                26 -> {
                    carModelList = resources.getStringArray(R.array.car_foton_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                27 -> {
                    carModelList = resources.getStringArray(R.array.car_hino_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                28 -> {
                    carModelList = resources.getStringArray(R.array.car_honda_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                29 -> {
                    carModelList = resources.getStringArray(R.array.car_hyundai_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                30 -> {
                    carModelList = resources.getStringArray(R.array.car_infiniti_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                31 -> {
                    carModelList = resources.getStringArray(R.array.car_isuzu_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                32 -> {
                    carModelList = resources.getStringArray(R.array.car_jaguar_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                33 -> {
                    carModelList = resources.getStringArray(R.array.car_jeep_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                34 -> {
                    carModelList = resources.getStringArray(R.array.car_kia_array)
                    checkedCarMake = which
                    checkedCarModel = -1
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                35 -> {
                    carModelList = resources.getStringArray(R.array.car_lamborghini_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                36 -> {
                    carModelList = resources.getStringArray(R.array.car_land_rover_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true

                }
                37 -> {
                    carModelList = resources.getStringArray(R.array.car_lexus_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                38 -> {
                    carModelList = resources.getStringArray(R.array.car_leyland_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                39 -> {
                    carModelList = resources.getStringArray(R.array.car_mahindra_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                40 -> {
                    carModelList = resources.getStringArray(R.array.car_man_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                41 -> {
                    carModelList = resources.getStringArray(R.array.car_messey_fergusson_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                42 -> {
                    carModelList = resources.getStringArray(R.array.car_mazda_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                43 -> {
                    carModelList = resources.getStringArray(R.array.car_mercedes_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                44 -> {
                    carModelList = resources.getStringArray(R.array.cars_nissan_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                45 -> {
                    carModelList = resources.getStringArray(R.array.car_opel_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                46 -> {
                    carModelList = resources.getStringArray(R.array.car_perodua_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                47 -> {
                    carModelList = resources.getStringArray(R.array.car_peugeot_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                48 -> {
                    carModelList = resources.getStringArray(R.array.car_porsche_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                49 -> {
                    carModelList = resources.getStringArray(R.array.car_renault_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                50 -> {
                    carModelList = resources.getStringArray(R.array.car_rover_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                51 -> {
                    carModelList = resources.getStringArray(R.array.car_royal_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                52 -> {
                    carModelList = resources.getStringArray(R.array.car_scania_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                53 -> {
                    carModelList = resources.getStringArray(R.array.car_shineray_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                54 -> {
                    carModelList = resources.getStringArray(R.array.car_sonalika_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                55 -> {
                    carModelList = resources.getStringArray(R.array.car_subaru_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                56 -> {
                    carModelList = resources.getStringArray(R.array.car_suzuki_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                57 -> {
                    carModelList = resources.getStringArray(R.array.car_tata_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                58 -> {
                    carModelList = resources.getStringArray(R.array.cars_toyota_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                59 -> {
                    carModelList = resources.getStringArray(R.array.car_trailer_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                60 -> {
                    carModelList = resources.getStringArray(R.array.motorcycle_tvs_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                61 -> {
                    carModelList = resources.getStringArray(R.array.car_vauxhall_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                62 -> {
                    carModelList = resources.getStringArray(R.array.car_vector_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                63 -> {
                    carModelList = resources.getStringArray(R.array.car_volkwagen_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                64 -> {
                    carModelList = resources.getStringArray(R.array.car_volvo_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                65 -> {
                    carModelList = resources.getStringArray(R.array.yamaha_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                66 -> {
                    carModelList = resources.getStringArray(R.array.car_zongshen_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
                67 -> {
                    carModelList = resources.getStringArray(R.array.car_zontes_array)
                    checkedCarMake = which
                    carMakeSelected = carMakeList[which]!!
                    binding.carMakeButton.text = carMakeList[which]
                    binding.carModelButton.isEnabled = true
                }
            }
        }

        carMakeListDialog.setPositiveButton("Ok") { _, _ ->
        }
        carMakeListDialog.setNegativeButton("Cancel", null)
        val dialog = carMakeListDialog.create()
        dialog.show()

    }

    private fun selectCarModel() {
        val carModelListDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val title = SpannableString((carMakeList[checkedCarMake]?.toUpperCase(Locale.ROOT)) + " MODELS")
        carMakeSelected = carMakeList[checkedCarMake].toString()
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carModelListDialog.setTitle(title)
        carModelListDialog.setSingleChoiceItems(carModelList, checkedCarModel) { _, which ->
            checkedCarModel = which
            binding.carModelButton.text = carModelList[which]
            carModelSelected = carModelList[which].toString()
        }
        carModelListDialog.setPositiveButton("Ok") { _, _ ->
        }
        carModelListDialog.setNegativeButton("Cancel", null)
        val dialog = carModelListDialog.create()
        dialog.show()
    }

    private fun selectCarYear() {
        val carYearDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carYearList = resources.getStringArray(R.array.cars_year_array)
        val title = SpannableString("SELECT CAR YEAR")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carYearDialog.setTitle(title)
        carYearDialog.setSingleChoiceItems(carYearList, checkedCarYear) { _, which ->
            checkedCarYear = which
            binding.carYearButton.text = carYearList[which]
            carYearSelected = carYearList[which].toString()
        }
        carYearDialog.setPositiveButton("Ok") { _, _ ->
        }
        carYearDialog.setNegativeButton("Cancel", null)
        val dialog = carYearDialog.create()
        dialog.show()
    }

    private fun selectCarBody() {
        val carBodyDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carBodyList = resources.getStringArray(R.array.cars_body_types_array)
        val title = SpannableString("SELECT CAR BODY")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carBodyDialog.setTitle(title)
        carBodyDialog.setSingleChoiceItems(carBodyList, checkedCarBody) { _, which ->
            checkedCarBody = which
            binding.carBodyButton.text = carBodyList[which]
            carBodyTypeSelected = carBodyList[which].toString()
        }
        carBodyDialog.setPositiveButton("Ok") { _, _ ->
        }
        carBodyDialog.setNegativeButton("Cancel", null)
        val dialog = carBodyDialog.create()
        dialog.show()
    }

    private fun selectCarCondition() {
        val carConditionDialog = androidx.appcompat.app.AlertDialog.Builder(this.requireContext())
        val carConditionList = resources.getStringArray(R.array.cars_condition_array)
        val title = SpannableString("SELECT CAR CONDITION")
        title.setSpan(ForegroundColorSpan(Color.parseColor("#009688")), 0, title.length, 0)
        carConditionDialog.setTitle(title)
        carConditionDialog.setSingleChoiceItems(carConditionList, checkedCarCondition) { _, which ->
            checkedCarCondition = which
            binding.carConditionButton.text = carConditionList[which]
            carConditionSelected = carConditionList[which].toString()
        }
        carConditionDialog.setPositiveButton("Ok") { _, _ ->
        }
        carConditionDialog.setNegativeButton("Cancel", null)
        val dialog = carConditionDialog.create()
        dialog.show()
    }

    private fun filterCarsList() {


        if (checkedCarMake >= 0) {
             filteredCarList = filteredCarList.filter { it.make == carMakeSelected }
        }

        if (checkedCarModel >= 0) {
            filteredCarList = filteredCarList.filter { it.model == carModelSelected }
        }


        if (checkedCarYear >= 0) {
            filteredCarList = filteredCarList.filter { it.year == carYearSelected.toInt() }
        }

        if (checkedCarBody >= 0) {
            filteredCarList = filteredCarList.filter { it.body == carBodyTypeSelected }
        }

        if (checkedCarCondition >= 0) {
            filteredCarList = filteredCarList.filter { it.condition == carConditionSelected }
        }
        dismiss()
        val bundle = bundleOf("filteredCarList" to filteredCarList)
        findNavController().navigate(R.id.filterCarsFragment, bundle)
    }

}
package org.carlistingapp.listingcarsproject.ui.home.profile.views
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.databinding.FragmentFeatureCarBinding

class FeatureCarFragment : Fragment() {
    private lateinit var binding: FragmentFeatureCarBinding
    private lateinit var car : CarObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater,R.layout.fragment_feature_car, container, false)
        car = (arguments?.getParcelable("carObject") as CarObject?)!!
        requireActivity().toolBar.title = "Sell Faster Your "+car.name

        binding.goldPackageButton.setOnClickListener {
            val bundle = bundleOf("carObject" to car)
            bundle.putString("Package", "3000")
            bundle.putString("Name", "Gold Package")
            val featureCarDialogFragment = FeatureCarDialog()
            featureCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
            featureCarDialogFragment.arguments = bundle
            featureCarDialogFragment.show(
                requireActivity().supportFragmentManager,
                "FeatureCarDialogFragment"
            )
        }

        binding.silverPackageButton.setOnClickListener {
            val bundle = bundleOf("carObject" to car)
            bundle.putString("Package", "1500")
            bundle.putString("Name", "Silver Package")
            val featureCarDialogFragment = FeatureCarDialog()
            featureCarDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0)
            featureCarDialogFragment.arguments = bundle
            featureCarDialogFragment.show(requireActivity().supportFragmentManager,"FeatureCarDialogFragment")
        }


        return binding.root
    }
}
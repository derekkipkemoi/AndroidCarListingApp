package org.carlistingapp.listingcarsproject.ui.home.postCar.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.databinding.FragmentPostCarImagesBinding
import org.carlistingapp.listingcarsproject.ui.home.HomeActivity
import org.carlistingapp.listingcarsproject.ui.home.postCar.adapters.MoreImagesAdapter
import org.carlistingapp.listingcarsproject.ui.home.postCar.viewModels.PostCarViewModel
import org.carlistingapp.listingcarsproject.ui.home.postCar.viewModels.PostCarViewModelFactory
import org.carlistingapp.listingcarsproject.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PostCarImagesFragment : Fragment() , KodeinAware{
    override val kodein by kodein()

    private val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
    private var selectedImageFront: Uri? = null
    private var selectedImageRight: Uri? = null
    private var selectedImageLeft: Uri? = null
    private var selectedImageBack: Uri? = null
    private var selectedImageDashBoard: Uri? = null
    private var selectedImageInterior: Uri? = null

    private val uploadImageList = ArrayList<Uri>()
    private var uploadImageMoreList = ArrayList<Uri>()
    private var imagePickerMoreList = ArrayList<com.nguyenhoanglam.imagepicker.model.Image>()
    private val uploadImageBodyPart = ArrayList<MultipartBody.Part>()

    private lateinit var binding: FragmentPostCarImagesBinding

    private val carDetailsToUpload : SharedPrefManager by instance()
    private val api : ListingCarsAPI by instance()
    private val repository : UserRepository by instance()
    private val factory: PostCarViewModelFactory by instance()
    private lateinit var viewModel: PostCarViewModel
    private val imageResizer : ImageResizer by instance()
    private lateinit var carObject: CarObject


    private lateinit var userID : String
    private lateinit var carID : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_post_car_images,
            container,
            false
        )
        val view = binding.root



        binding.frontImagePicker.setOnClickListener {
//            Intent(Intent.ACTION_PICK).also {
//                it.type = "image/*"
//                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//                startActivityForResult(it, FRONT_IMAGE_REQUEST_CODE)
//            }
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(FRONT_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.rightImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(RIGHT_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.leftImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(LEFT_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.backImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(BACK_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.dashBoardImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(DASHBOARD_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.interiorImagePicker.setOnClickListener {
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Album")
                .setDirectoryName("Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(1)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(INTERIOR_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.selectMoreImagesButton.setOnClickListener {
            uploadImageMoreList.clear()
            ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle("Moti Kenya Listing")
                .setDirectoryName("Moti Image Picker")
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(6)
                .setLimitMessage("You can select up to 10 images")
                .setRequestCode(MULTIPLE_IMAGE_REQUEST_CODE)
                .start();
        }

        binding.buttonBack.setOnClickListener {
            val viewPager2 = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager2?.currentItem = 1
        }
        binding.buttonNext.setOnClickListener {
            posCarDetails()
        }


        return view
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ){
            when (requestCode){
                FRONT_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageFront = images[0].uri
                    Toast.makeText(requireContext(), selectedImageFront.toString(), Toast.LENGTH_LONG).show()
                    binding.frontImage.setImageURI(selectedImageFront)
                    if (uploadImageList.contains(selectedImageFront!!)) {
                        uploadImageList.remove(selectedImageFront!!)
                    }
                    uploadImageList.add(selectedImageFront!!)
                }
                RIGHT_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageRight = images[0].uri
                    binding.rightImage.setImageURI(selectedImageRight)
                    if (uploadImageList.contains(selectedImageRight!!)) {
                        uploadImageList.remove(selectedImageRight!!)
                    }
                    uploadImageList.add(selectedImageRight!!)
                }
                LEFT_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageLeft = images[0].uri
                    binding.leftImage.setImageURI(selectedImageLeft)
                    if (uploadImageList.contains(selectedImageLeft!!)) {
                        uploadImageList.remove(selectedImageLeft!!)
                    }
                    uploadImageList.add(selectedImageLeft!!)
                }
                BACK_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageBack = images[0].uri
                    binding.backImage.setImageURI(selectedImageBack)
                    if (uploadImageList.contains(selectedImageBack!!)) {
                        uploadImageList.remove(selectedImageBack!!)
                    }
                    uploadImageList.add(selectedImageBack!!)

                }
                DASHBOARD_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageDashBoard = images[0].uri
                    binding.dashBoardImage.setImageURI(selectedImageDashBoard)
                    if (uploadImageList.contains(selectedImageDashBoard!!)) {
                        uploadImageList.remove(selectedImageDashBoard!!)
                    }
                    uploadImageList.add(selectedImageDashBoard!!)
                }

                INTERIOR_IMAGE_REQUEST_CODE -> {
                    val images = ImagePicker.getImages(data)
                    selectedImageInterior = images[0].uri
                    binding.interiorImage.setImageURI(selectedImageInterior)
                    if (uploadImageList.contains(selectedImageInterior!!)) {
                        uploadImageList.remove(selectedImageInterior!!)
                    }
                    uploadImageList.add(selectedImageInterior!!)
                }
                MULTIPLE_IMAGE_REQUEST_CODE -> {
                    imagePickerMoreList = ImagePicker.getImages(data)
                    for (imagePicked in imagePickerMoreList){
                        uploadImageMoreList.add(imagePicked.uri)
                    }
                    imagePickerMoreList.clear()
                    binding.recyclerviewMoreImages.also {
                        it.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.setHasFixedSize(true)
                        it.adapter = MoreImagesAdapter(uploadImageMoreList, requireContext())
                    }
                }

            }
        }
    }
    companion object{
        private const val FRONT_IMAGE_REQUEST_CODE = 100
        private const val RIGHT_IMAGE_REQUEST_CODE = 110
        private const val LEFT_IMAGE_REQUEST_CODE = 120
        private const val BACK_IMAGE_REQUEST_CODE = 130
        private const val DASHBOARD_IMAGE_REQUEST_CODE = 140
        private const val INTERIOR_IMAGE_REQUEST_CODE = 150
        private const val MULTIPLE_IMAGE_REQUEST_CODE = 160
    }

    private fun posCarDetails() {
        viewModel = ViewModelProvider(this.requireActivity(), factory).get(PostCarViewModel::class.java)
        if (selectedImageFront == null || selectedImageRight == null || selectedImageLeft == null || selectedImageBack == null ||
                selectedImageDashBoard == null || selectedImageInterior == null) {
            binding.root.snackBar("Front | RightSide| LeftSide | BackSide| DashBoard | Interior | Images are Required")
            //Toast.makeText(this.requireContext(),"Hallo Everyone",Toast.LENGTH_LONG).show()
            return
        }
        for (image in uploadImageMoreList){
            uploadImageList.add(image)
        }
        uploadImageMoreList.clear()

        val car = CarObject(
            carDetailsToUpload.getCarName(),
            carDetailsToUpload.getCarMake(),
            carDetailsToUpload.getCarModel(),
            carDetailsToUpload.getCarYear(),
            carDetailsToUpload.getCarBody(),
            carDetailsToUpload.getCarCondition(),
            carDetailsToUpload.getCarTransmission(),
            carDetailsToUpload.getCarDuty(),
            carDetailsToUpload.getCarMileage(),
            carDetailsToUpload.getCarPrice(),
            carDetailsToUpload.getCarPriceNegotiable(),
            carDetailsToUpload.getCarFuel(),
            carDetailsToUpload.getCarInterior(),
            carDetailsToUpload.getCarColor(),
            carDetailsToUpload.getCarEngine(),
            carDetailsToUpload.getCarDescription(),
            carDetailsToUpload.getCarCommonFeatures(),
            carDetailsToUpload.getCarLocation()
        )


        if (carDetailsToUpload.getSession() != "userLoggedOut"){
            userID = carDetailsToUpload.getSession().toString()
            val customAlertDialog = CustomAlertDialog(requireActivity())
            customAlertDialog.startLoadingDialog("Uploading Car")
            try {
            viewModel.postUserCar(car, userID)
                viewModel.postCarResponse.observe(viewLifecycleOwner, Observer {
                    if (it._id != null) {
                        carID = it._id
                        for (imageFile in uploadImageList) {
                            val imageStream = requireContext().contentResolver.openInputStream(imageFile)
                            val fullSizeBitmap = BitmapFactory.decodeStream(imageStream)
                            val reducedImageBitMap = imageResizer.reduceBitmapSize(fullSizeBitmap, 614400)
                            val reducedImageFile = getFileFromBitmap(reducedImageBitMap,imageFile)

//                            val parcelFileDescriptor = requireContext().contentResolver?.openFileDescriptor(imageFile, "r", null) ?: return@Observer
//                            val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(imageFile))
//                            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
//                            val outputStream = FileOutputStream(file)
//                            inputStream.copyTo(outputStream)
                            //val body = UploadImageRequestBody(file,"image")
                            val body = RequestBody.create(MediaType.parse("image/*"), reducedImageFile)
                            val multipartBody = MultipartBody.Part.createFormData("photos", reducedImageFile.name, body)
                            uploadImageBodyPart.add(multipartBody)
                        }

                        viewModel.postUserCarImages(uploadImageBodyPart, carID)
                        viewModel.postCarImagesResponse.observe(
                            viewLifecycleOwner,
                            Observer { postCarImagesResponse ->
                                carObject = postCarImagesResponse
                                customAlertDialog.stopDialog()
                                Toast.makeText(
                                    requireContext(),
                                    "Car ${postCarImagesResponse.make + postCarImagesResponse.model + postCarImagesResponse.year} Uploaded Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                uploadImageBodyPart.clear()
                                uploadImageList.clear()
                                val session: SharedPreferences =
                                    requireActivity().getSharedPreferences(
                                        "privatePreferenceName",
                                        Context.MODE_PRIVATE
                                    )
                                session.edit().remove("carNameKey").apply()
                                session.edit().remove("carMakeKey").apply()
                                session.edit().remove("carModelKey").apply()
                                session.edit().remove("carYearKey").apply()
                                session.edit().remove("carBodyKey").apply()
                                session.edit().remove("carConditionKey").apply()
                                session.edit().remove("carTransmissionKey").apply()
                                session.edit().remove("carDutyKey").apply()
                                session.edit().remove("carMileageKey").apply()
                                session.edit().remove("carPriceKey").apply()
                                session.edit().remove("carPriceNegotiableKey").apply()
                                session.edit().remove("carFuelKey").apply()
                                session.edit().remove("carInteriorKey").apply()
                                session.edit().remove("carColorKey").apply()
                                session.edit().remove("carEngineSizeKey").apply()
                                session.edit().remove("carDescriptionKey").apply()
                                session.edit().remove("carCommonFeaturesKey").apply()
                                session.edit().remove("carLocationKey").apply();
                                requireContext().cacheDir.deleteRecursively()
                                loadHomeActivity()
                            })

                    }
                })
            }catch (e: NoInternetException){
                binding.root.snackBar(e.message)
            }

        }
    }

    private fun getFileFromBitmap(reducedBitmap: Bitmap?, imageFile: Uri) : File{
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(imageFile))
        val bitmapOutputStream = ByteArrayOutputStream()
        reducedBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, bitmapOutputStream)
        val bitmapData = bitmapOutputStream.toByteArray()
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(bitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()
        return file
    }

    private fun loadHomeActivity() {
        activity?.finish()
       val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
//        val bundle = bundleOf("carObject" to carObject)
//        Navigation.findNavController(requireActivity(), R.id.fragment).navigate(R.id.featureCarFragment, bundle)
    }
}





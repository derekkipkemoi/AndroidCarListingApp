package org.carlistingapp.listingcarsproject.ui.home.postCar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import org.carlistingapp.listingcarsproject.Coroutines
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.data.repository.UserRepository

class PostCarViewModel(private val repository: UserRepository) : ViewModel() {

    private val _postCarResponse = MutableLiveData<CarObject>()
    val postCarResponse : LiveData<CarObject> get() = _postCarResponse

    private val _postCarImagesResponse = MutableLiveData<CarObject>()
    val postCarImagesResponse : LiveData<CarObject> get() = _postCarImagesResponse
    private lateinit var job: Job


    fun postUserCar(carObject : CarObject, userId : String)
    {
        job = Coroutines.ioThenMain({repository.postUserCar(carObject,userId )},{_postCarResponse.value = it})
    }


    fun postUserCarImages(carObjectImages: List<MultipartBody.Part>, carId : String?)
    {
        job = Coroutines.ioThenMain({repository.postUserCarImages(carObjectImages,carId)},{_postCarImagesResponse.value = it})
    }

    override fun onCleared()
    {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}
package org.carlistingapp.listingcarsproject.ui.home.profile.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.carlistingapp.listingcarsproject.Coroutines
import org.carlistingapp.listingcarsproject.data.db.entities.*
import org.carlistingapp.listingcarsproject.data.network.response.AuthResponse
import org.carlistingapp.listingcarsproject.data.network.response.CarObjectResponse
import org.carlistingapp.listingcarsproject.data.network.response.Message
import org.carlistingapp.listingcarsproject.data.repository.UserRepository
import org.carlistingapp.listingcarsproject.utils.SharedPrefManager

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userCars = MutableLiveData<CarObjectResponse>()
    val userCars : LiveData<CarObjectResponse> get() = _userCars

    private val _getUpdateUserCar = MutableLiveData<CarObject>()
    val getUpdateUserCar : LiveData<CarObject> get() = _getUpdateUserCar

    private val _deleteUserCar = MutableLiveData<Message>()
    val deleteUserCar : LiveData<Message> get() = _deleteUserCar

    private val _featureCar = MutableLiveData<Message>()
    val featureCar : LiveData<Message> get() = _featureCar


    private val _updateName = MutableLiveData<Message>()
    val updateName : LiveData<Message> get() = _updateName

    private val _userPhoneObject = MutableLiveData<AuthResponse>()
    val userPhoneObject : LiveData<AuthResponse> = _userPhoneObject

    private lateinit var session : SharedPrefManager

    private lateinit var job: Job

    fun getUserCars(userID : String){
        job = Coroutines.ioThenMain({userRepository.getUserCars(userID)}, {_userCars.value = it})
    }

    fun updateUserCar(carObject: CarUpdate , carId : String){
        job = Coroutines.ioThenMain({userRepository.updateUserCar(carObject, carId)}, {_getUpdateUserCar.value = it} )
    }

    fun updateUserName(name: NameUpdate, userId : String){
        job = Coroutines.ioThenMain({userRepository.updateUserName(name, userId)}, {_updateName.value = it} )
    }

    fun verifyUserPhoneNumber(context: Context, phoneNumber: PhoneNumber){
        session = SharedPrefManager(context)
        if (session.getSession() != "userLoggedOut")
        {
            val userId : String? = session.getSession()
            job = Coroutines.ioThenMain({userRepository.verifyUserPhoneNumber(phoneNumber,userId )},{_userPhoneObject.value = it})
        }
    }

    fun deleteUserCar(carId : String){
        job = Coroutines.ioThenMain({userRepository.deleteUserCar(carId)}, {_deleteUserCar.value = it} )
    }

    fun featureCar(carId: String, featureUserCar: FeatureUserCar){
        job = Coroutines.ioThenMain({userRepository.featureUserCar(carId, featureUserCar)}, {_featureCar.value = it} )
    }


    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}
package org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.carlistingapp.listingcarsproject.Coroutines
import org.carlistingapp.listingcarsproject.data.db.entities.UsersObject
import org.carlistingapp.listingcarsproject.data.network.response.CarObjectResponse
import org.carlistingapp.listingcarsproject.data.repository.UserRepository

class ListCarViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _cars = MutableLiveData<CarObjectResponse>()
    val cars : LiveData<CarObjectResponse> get() = _cars
    private lateinit var job: Job

    fun getCars(){
        job = Coroutines.ioThenMain({userRepository.getCars()}, {_cars.value = it})
    }


    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}
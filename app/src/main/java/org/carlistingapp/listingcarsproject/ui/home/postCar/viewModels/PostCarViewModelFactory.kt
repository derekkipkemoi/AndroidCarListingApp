package org.carlistingapp.listingcarsproject.ui.home.postCar.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.carlistingapp.listingcarsproject.data.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class PostCarViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostCarViewModel(userRepository) as T
    }
}
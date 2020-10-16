package org.carlistingapp.listingcarsproject.ui.home.listCar.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.carlistingapp.listingcarsproject.data.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class ListCarViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListCarViewModel(userRepository) as T
    }
}
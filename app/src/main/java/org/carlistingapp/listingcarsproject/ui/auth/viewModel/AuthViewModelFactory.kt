package org.carlistingapp.listingcarsproject.ui.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(
            authRepository
        ) as T
    }
}
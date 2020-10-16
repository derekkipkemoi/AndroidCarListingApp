package org.carlistingapp.listingcarsproject.ui.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.carlistingapp.listingcarsproject.Coroutines
import org.carlistingapp.listingcarsproject.data.db.entities.User
import org.carlistingapp.listingcarsproject.data.network.response.AuthResponse
import org.carlistingapp.listingcarsproject.data.db.entities.GoogleSignInAccessTokenDataClass
import org.carlistingapp.listingcarsproject.data.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel(){

    private lateinit var job: Job
    private val _loggedInUserResponse = MutableLiveData<AuthResponse>()
    val loggedInUserResponse : LiveData<AuthResponse> get() = _loggedInUserResponse

    private val _loggedInUseWithGoogleAccountResponse = MutableLiveData<AuthResponse>()
    val loggedInUseWithGoogleAccountResponse : LiveData<AuthResponse> get() = _loggedInUseWithGoogleAccountResponse

    private val _googleAccessToken = MutableLiveData<GoogleSignInAccessTokenDataClass>()
    val googleAccessToken : LiveData<GoogleSignInAccessTokenDataClass> get() = _googleAccessToken

    private val _loggedInUseWithFacebookAccountResponse = MutableLiveData<AuthResponse>()
    val loggedInUseWithFacebookAccountResponse : LiveData<AuthResponse> get() = _loggedInUseWithFacebookAccountResponse


    private val _registeredUserResponse = MutableLiveData<AuthResponse>()
    val registeredUserResponse : LiveData<AuthResponse> get() = _registeredUserResponse


    fun getSignedUpUser(email: String, password: String, name: String)  {
        val usersObject = User(email,password,name)
            job = Coroutines.ioThenMain({repository.userSignUp(usersObject)},{_registeredUserResponse.value = it})
    }

    fun getSignedInWithGoogleAccount(token: String)  {
        val userToken = AuthResponse(token)
            job = Coroutines.ioThenMain({repository.userSignInWithGoogleAccount(userToken)},{_loggedInUseWithGoogleAccountResponse.value = it})
    }


    fun getSignedInWithFacebookAccount(token: String)  {
        val userToken = AuthResponse(token)
        job = Coroutines.ioThenMain({repository.userSignInWithFacebookAccount(userToken)},{_loggedInUseWithFacebookAccountResponse.value = it})
    }

    fun getLoggedInUser(email :String, password : String) {
        val userObject = User(email,password)
        job = Coroutines.ioThenMain({
            repository.userLogin(userObject)}, { _loggedInUserResponse.value = it
        })
    }


    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}
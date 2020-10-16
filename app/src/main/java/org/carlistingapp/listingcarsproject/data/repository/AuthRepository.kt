package org.carlistingapp.listingcarsproject.data.repository

import org.carlistingapp.listingcarsproject.data.db.entities.PhoneNumber
import org.carlistingapp.listingcarsproject.data.db.entities.User
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.SafeAPIRequest
import org.carlistingapp.listingcarsproject.data.network.response.AuthResponse

class AuthRepository(private val listingCarsAPI: ListingCarsAPI) : SafeAPIRequest() {
    //suspend fun getUsers() = apiRequest { listingCarsAPI.getUsers() }
    suspend fun userSignUp(user: User) : AuthResponse{
        return apiRequest { listingCarsAPI.userSignUp(user)}
    }

    suspend fun userLogin(user: User) : AuthResponse {
        return apiRequest {listingCarsAPI.getUserLogin(user)}
    }

    suspend fun userSignInWithGoogleAccount(token: AuthResponse) = apiRequest {listingCarsAPI.getUserGoogleSignIn(token)}

    suspend fun userSignInWithFacebookAccount(token: AuthResponse) = apiRequest {listingCarsAPI.getUserFacebookSignIn(token)}


}
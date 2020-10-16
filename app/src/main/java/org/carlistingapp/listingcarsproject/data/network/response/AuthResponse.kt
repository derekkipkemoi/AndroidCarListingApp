package org.carlistingapp.listingcarsproject.data.network.response

import android.os.Message
import org.carlistingapp.listingcarsproject.data.db.entities.UsersObject

data class AuthResponse(
    val access_token: String? = null
){
    val message: String? = null
    val userObject: UsersObject? = null
}


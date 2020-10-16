package org.carlistingapp.listingcarsproject.data.db.entities

data class UsersObject(
    val id: String? = null,
    val cars: List<String>? = null,
    val method: String? = null,
    val local: User? = null,
    val facebook: User? = null,
    val google: User? = null,
    val phoneNumber: PhoneNumber? = null
)
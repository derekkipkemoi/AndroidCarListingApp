package org.carlistingapp.listingcarsproject.data.db.entities

data class CarUpdate(
    val condition: String,
    val duty: String,
    val location: String,
    val mileage: Int,
    val description: String,
    val price: Int,
    val priceNegotiable: Boolean
)
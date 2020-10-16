package org.carlistingapp.listingcarsproject.data.db.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class CarObject(
    val name: String? = null,
    val make: String? = null,
    val model: String? =null,
    val year: Int? = null,
    val body: String? = null,
    val condition: String? = null,
    val transmission: String? = null,
    val duty: String? = null,
    var mileage: Int? = null,
    val price: Int? = null,
    val priceNegotiable: Boolean? = true,
    val fuel: String? =null,
    val interior: String? = null,
    val color: String? = null,
    val engineSize: Int? =null,
    val description: String? = null,
    val features: MutableSet<String>? = null,
    val location: String? = null,
    val images: List<String>? = null
) : Parcelable {
    val _id: String? = null
    val seller: Seller? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        TODO("features"),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(make)
        parcel.writeString(model)
        parcel.writeValue(year)
        parcel.writeString(body)
        parcel.writeString(condition)
        parcel.writeString(transmission)
        parcel.writeString(duty)
        parcel.writeValue(mileage)
        parcel.writeValue(price)
        parcel.writeValue(priceNegotiable)
        parcel.writeString(fuel)
        parcel.writeString(interior)
        parcel.writeString(color)
        parcel.writeValue(engineSize)
        parcel.writeString(description)
        parcel.writeString(location)
        parcel.writeStringList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarObject> {
        override fun createFromParcel(parcel: Parcel): CarObject {
            return CarObject(parcel)
        }

        override fun newArray(size: Int): Array<CarObject?> {
            return arrayOfNulls(size)
        }
    }
}





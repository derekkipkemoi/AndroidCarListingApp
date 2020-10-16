package org.carlistingapp.listingcarsproject.data.repository

import okhttp3.MultipartBody
import org.carlistingapp.listingcarsproject.data.db.entities.*
import org.carlistingapp.listingcarsproject.data.network.ListingCarsAPI
import org.carlistingapp.listingcarsproject.data.network.SafeAPIRequest
import org.carlistingapp.listingcarsproject.data.network.response.AuthResponse
import org.carlistingapp.listingcarsproject.data.network.response.Message

class UserRepository(private val listingCarsAPI: ListingCarsAPI) : SafeAPIRequest(){
    //Get All Cars
    suspend fun getCars() = apiRequest { listingCarsAPI.getCars() }

    //Post User Cars
    suspend fun postUserCar(carObject: CarObject, userId : String?) = apiRequest { listingCarsAPI.postCar(carObject,userId) }


    //Get user Cars
    suspend fun getUserCars(userId : String?) = apiRequest { listingCarsAPI.getUserCars(userId) }

    //Delete user Cars
    suspend fun deleteUserCar(carId: String?) : Message {
        return apiRequest { listingCarsAPI.deleteUserCar(carId) }
    }

    //Feature user Cars
    suspend fun featureUserCar(carId: String?, featureUserCar: FeatureUserCar) : Message {
        return  apiRequest { listingCarsAPI.featureUserCar(carId, featureUserCar) }
    }

    //Update User Car
    suspend fun updateUserCar(carUpdate: CarUpdate, carId: String?) : CarObject {
       return apiRequest { listingCarsAPI.updateUserCar(carUpdate, carId) }
    }

    //Update User
    suspend fun updateUserName(name : NameUpdate, userId : String) = apiRequest {listingCarsAPI.updateUserName(name, userId)}

    //Verify User Phone
    suspend fun verifyUserPhoneNumber(phoneNumber : PhoneNumber , userId : String?) : AuthResponse {
        return apiRequest { listingCarsAPI.verifyUserPhone(phoneNumber,userId) }
    }

    //Post User Car Images
    suspend fun postUserCarImages(carObjectImages: List<MultipartBody.Part>, carId : String?) = apiRequest { listingCarsAPI.postCarImages(carObjectImages,carId) }

}
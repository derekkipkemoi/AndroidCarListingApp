package org.carlistingapp.listingcarsproject.data.network

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.carlistingapp.listingcarsproject.data.db.entities.*
import org.carlistingapp.listingcarsproject.data.network.response.AuthResponse
import org.carlistingapp.listingcarsproject.data.network.response.CarObjectResponse
import org.carlistingapp.listingcarsproject.data.network.response.Message
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ListingCarsAPI {

    @Headers("Content-Type: application/json")
    @POST("users/registerUser")
    suspend fun userSignUp(@Body user: User) : Response<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("users/loginUser")
    suspend fun getUserLogin(@Body user: User) : Response<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("users/oauth/google")
    suspend fun getUserGoogleSignIn(@Body token: AuthResponse) : Response<AuthResponse>

    @FormUrlEncoded
    @POST
    fun getAccessTokenGoogle(
        @Url url: String,
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") authCode: String,
        @Field("id_token") id_token: String
    ): Call<GoogleSignInAccessTokenDataClass>

//    @Headers("Content-Type: application/json")
//    @POST
//    suspend fun paymentSTKPush(
//        @Header("Authorization") authorization : String,
//        @Url url: String,
//        @Body stkPush: PaymentSTKPush
//    ) : Response<PaymentSTKPushResponse>


    @Headers("Content-Type: application/json")
    @POST("users/oauth/facebook")
    suspend fun getUserFacebookSignIn(@Body token: AuthResponse) : Response<AuthResponse>

    @GET("users/{userId}/cars")
    suspend fun getUserCars(@Path("userId") userId:String?) : Response<CarObjectResponse>

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/phoneNumber")
    suspend fun verifyUserPhone(@Body phoneNumberObject: PhoneNumber, @Path("userId") userId:String?) : Response<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("users/{id}/cars")
    suspend fun postCar(@Body car: CarObject, @Path("id") id:String?) : Response<CarObject>

    @GET("cars/listCars")
    suspend fun getCars() : Response<CarObjectResponse>

    @Headers("Content-Type: application/json")
    @PATCH("cars/{carId}")
    suspend fun updateUserCar(@Body carObject: CarUpdate, @Path("carId") carId:String?) : Response<CarObject>

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}")
    suspend fun updateUserName(@Body name: NameUpdate, @Path("userId") userId:String?) : Response<Message>

    @Headers("Content-Type: application/json")
    @DELETE("cars/{carId}")
    suspend fun deleteUserCar(@Path("carId") carId:String?) : Response<Message>

    @Headers("Content-Type: application/json")
    @PATCH("cars/{carId}/featureCar")
    suspend fun featureUserCar(@Path("carId") carId:String?, @Body featureUserCar: FeatureUserCar) : Response<Message>

    @Multipart
    @POST("cars/{id}/carImages")
    suspend fun postCarImages(@Part photos: List<MultipartBody.Part>, @Path("id") id:String?) : Response<CarObject>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : ListingCarsAPI{
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                .readTimeout(2, TimeUnit.MINUTES) // read timeout
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.api.motikenya.co.ke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ListingCarsAPI::class.java)
        }
    }
}
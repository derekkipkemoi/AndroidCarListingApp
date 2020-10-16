package org.carlistingapp.listingcarsproject.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val userSessionKey : String = "userID"
    private val userNameKey : String = "userName"
    private val userEmailKey : String = "userEmail"
    private val userPhoneNumberKey : String = "userPhoneNumber"
    private val userPhoneNumberVerifiedKey : String = "userPhoneNumberVerified"
    private val userSignInMethodKey : String = "userSignInMethod"


    private val carNameKey : String = "carNameKey"
    private val carMakeKey : String = "carMakeKey"
    private val carModelKey : String = "carModelKey"
    private val carYearKey : String = "carYearKey"
    private val carBodyKey : String = "carBodyKey"
    private val carConditionKey : String = "carConditionKey"
    private val carTransmissionKey : String = "carTransmissionKey"
    private val carDutyKey : String = "carDutyKey"
    private val carMileageKey : String = "carMileageKey"
    private val carPriceKey : String = "carPriceKey"
    private val carPriceNegotiableKey : String = "carPriceNegotiableKey"
    private val carFuelKey : String = "carFuelKey"
    private val carInteriorKey : String = "carInteriorKey"
    private val carColorKey : String = "carColorKey"
    private val carEngineSizeKey : String = "carEngineSizeKey"
    private val carDescriptionKey : String = "carDescriptionKey"
    private val carCommonFeaturesKey : String = "carCommonFeaturesKey"
    private val carLocationKey : String = "carLocationKey"

    private val privatePreferenceName : String = "privatePreferenceName"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(privatePreferenceName,Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sharedPreferences.edit()

    fun saveSession(userID: String?){
        editor.putString(userSessionKey,userID).commit()
    }
    fun getSession() : String? {
        return sharedPreferences.getString(userSessionKey,"userLoggedOut")
    }

    fun saveUserName(userName : String){
       editor.putString(userNameKey, userName).commit()
    }

    fun  getUserName() : String?{
        return sharedPreferences.getString(userNameKey,"noUserName")
    }

    fun saveUserEmail(userEmail : String){
        editor.putString(userEmailKey, userEmail).commit()
    }

    fun  getUserEmail() : String?{
        return sharedPreferences.getString(userEmailKey,"noUserEmail")
    }

    fun saveUserPhoneNumber(userPhoneNumber : Int){
        editor.putInt(userPhoneNumberKey, userPhoneNumber).commit()
    }

    fun  getUserPhoneNumber() : Int?{
        return sharedPreferences.getInt(userPhoneNumberKey,0)
    }

    fun saveUserPhoneNumberVerified(userPhoneNumberVerified : Boolean){
        editor.putBoolean(userPhoneNumberVerifiedKey, userPhoneNumberVerified).commit()
    }

    fun  getUserPhoneNumberVerified() : Boolean{
        return sharedPreferences.getBoolean(userPhoneNumberVerifiedKey,false)
    }

    fun saveUserSignInMethod(userSignInMethod : String){
        editor.putString(userSignInMethodKey, userSignInMethod).commit()
    }

    fun  getUserSignInMethod() : String?{
        return sharedPreferences.getString(userSignInMethodKey,"noMethod")
    }

    fun  saveCarName(carName: String){
        editor.putString(carNameKey, carName).commit()
    }

    fun getCarName() : String?{
        return sharedPreferences.getString(carNameKey,"carNameNotAdded")
    }

    fun  saveCarMake(carMake: String){
        editor.putString(carMakeKey, carMake.toString()).commit()
    }
    fun getCarMake() : String?{
        return sharedPreferences.getString(carMakeKey,"carMakeNotAdded")
    }

    fun  saveCarModel(carModel: String){
        editor.putString(carModelKey, carModel.toString()).commit()
    }
    fun getCarModel() : String?{
        return sharedPreferences.getString(carModelKey,"carModelNotAdded")
    }

    fun  saveCarYear(carYear: Int){
        editor.putInt(carYearKey, carYear.toInt()).commit()
    }
    fun getCarYear() : Int?{
        return sharedPreferences.getInt(carYearKey,0)
    }

    fun  saveCarBody(carBody: String){
        editor.putString(carBodyKey, carBody.toString()).commit()
    }
    fun getCarBody() : String?{
        return sharedPreferences.getString(carBodyKey,"carBodyNotAdded")
    }

    fun  saveCarCondition(carCondition: String){
        editor.putString(carConditionKey, carCondition.toString()).commit()
    }
    fun getCarCondition() : String?{
        return sharedPreferences.getString(carConditionKey,"carConditionNotAdded")
    }

    fun  saveCarTransmission(carTransmission: String){
        editor.putString(carTransmissionKey, carTransmission.toString()).commit()
    }
    fun getCarTransmission() : String?{
        return sharedPreferences.getString(carTransmissionKey,"carTransmissionNotAdded")
    }

    fun  saveCarDuty(carDuty: String){
        editor.putString(carDutyKey, carDuty.toString()).commit()
    }
    fun getCarDuty() : String?{
        return sharedPreferences.getString(carDutyKey,"carDutyNotAdded")
    }

    fun  saveCarMileage(carMileage: Int){
        editor.putInt(carMileageKey, carMileage.toInt()).commit()
    }
    fun getCarMileage() : Int?{
        return sharedPreferences.getInt(carMileageKey,-10)
    }

    fun  saveCarPrice(carPrice: Int){
        editor.putInt(carPriceKey, carPrice.toInt()).commit()
    }
    fun getCarPrice() : Int?{
        return sharedPreferences.getInt(carPriceKey,-10)
    }

    fun  saveCarPriceNegotiable(carPriceNegotiable: Boolean){
        editor.putBoolean(carPriceNegotiableKey, carPriceNegotiable).commit()
    }
    fun getCarPriceNegotiable() : Boolean?{
        return sharedPreferences.getBoolean(carPriceNegotiableKey,false)
    }

    fun  saveCarFuel(carFuel: String){
        editor.putString(carFuelKey, carFuel).commit()
    }
    fun getCarFuel() : String?{
        return sharedPreferences.getString(carFuelKey,"carFuelNotAdded")
    }

    fun  saveCarInterior(carInterior: String){
        editor.putString(carInteriorKey, carInterior).commit()
    }
    fun getCarInterior() : String?{
        return sharedPreferences.getString(carInteriorKey,"carInteriorNotAdded")
    }

    fun  saveCarColor(carColor: String){
        editor.putString(carColorKey, carColor).commit()
    }
    fun getCarColor() : String?{
        return sharedPreferences.getString(carColorKey,"carColorNotAdded")
    }

    fun  saveCarEngineSize(carEngineSize: Int){
        editor.putInt(carEngineSizeKey, carEngineSize).commit()
    }
    fun getCarEngine() : Int?{
        return sharedPreferences.getInt(carEngineSizeKey,-10)
    }

    fun  saveCarDescription(carDescription: String){
        editor.putString(carDescriptionKey, carDescription).commit()
    }
    fun getCarDescription() : String?{
        return sharedPreferences.getString(carDescriptionKey,"carDescriptionNotAdded")
    }

    fun  saveCarCommonFeatures(carCommonFeatures: HashSet<String>){
        editor.putStringSet(carCommonFeaturesKey,carCommonFeatures).commit()
    }
    fun getCarCommonFeatures() : MutableSet<String>? {
        return sharedPreferences.getStringSet(carCommonFeaturesKey,null)
    }


    fun  saveCarLocation(carLocation: String){
        editor.putString(carLocationKey,carLocation).commit()
    }
    fun getCarLocation() : String? {
        return sharedPreferences.getString(carLocationKey,"carLocationNotAdded")
    }
}
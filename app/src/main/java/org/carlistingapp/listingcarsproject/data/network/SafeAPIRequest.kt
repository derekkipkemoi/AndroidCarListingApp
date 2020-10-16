package org.carlistingapp.listingcarsproject.data.network

import android.content.ContentValues.TAG
import android.util.Log
import org.carlistingapp.listingcarsproject.utils.ApiException
import org.carlistingapp.listingcarsproject.utils.NoInternetException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

abstract class SafeAPIRequest {

    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if (response.isSuccessful){
            return response.body()!!
        } else{
            val error = response.errorBody()?.string()
            Log.d(TAG, error!!)
            val errorMessage = StringBuilder()
            error.let {
                try {
                    errorMessage.append(JSONObject(it).getJSONObject("error"))
                }catch (e : JSONException){ }
                errorMessage.append("\n")
            }
            errorMessage.append("Error Code: ${response.code()}")
            throw ApiException(errorMessage.toString())
        }
    }
}


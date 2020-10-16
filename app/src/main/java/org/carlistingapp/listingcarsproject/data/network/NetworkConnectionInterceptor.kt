package org.carlistingapp.listingcarsproject.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import org.carlistingapp.listingcarsproject.utils.NoInternetException


class NetworkConnectionInterceptor(context: Context) : Interceptor {
    private val applicationContext = context.applicationContext


    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkConnected()){
            //Toast.makeText(applicationContext,"No internet",Toast.LENGTH_LONG).show()
            throw NoInternetException("No internet connection")
            //Log.d("NetworkActive error ","Hallo guy")
           //throw NoInternetException("No internet")
        }

        return chain.proceed(chain.request())
    }

    private fun isNetworkConnected() : Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities.also {
            if (it != null){
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
            }
        }
        return false
    }
}
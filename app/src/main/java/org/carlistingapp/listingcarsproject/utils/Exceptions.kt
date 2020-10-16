package org.carlistingapp.listingcarsproject.utils

import android.os.Message
import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
package kz.amangeldy.flickrapplication.presentation

import android.content.res.Resources
import kz.amangeldy.flickrapplication.R
import java.lang.Exception
import java.net.UnknownHostException

class ExceptionMessageFactory(
    private val resource: Resources
) {

    fun createMessage(exception: Exception): String? {
        return when (exception) {
            is UnknownHostException -> resource.getString(R.string.error_no_connection)
            else -> null
        }
    }
}
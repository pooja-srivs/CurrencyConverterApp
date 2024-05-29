package com.example.currencyconverterapp.data.apiservice.interceptor

import android.content.Context
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.common.NetworkUtility
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val context: Context
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtility.isInternet(context)) {
            throw IOException(context.getString(R.string.no_internet_connection))
        }
        return chain.proceed(chain.request())
    }
}
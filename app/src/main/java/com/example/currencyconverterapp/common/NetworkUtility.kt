package com.example.currencyconverterapp.common

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtility {

    fun isInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        return network != null
    }
}
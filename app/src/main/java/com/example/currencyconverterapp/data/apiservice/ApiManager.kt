package com.example.currencyconverterapp.data.apiservice

import retrofit2.Retrofit

class ApiManager(private val retrofit: Retrofit) {

    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
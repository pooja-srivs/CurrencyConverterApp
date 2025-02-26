package com.example.currencyconverterapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

    @Provides
    fun provideClock(): Clock {
        return Clock.systemDefaultZone()
    }
}
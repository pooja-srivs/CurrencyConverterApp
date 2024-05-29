package com.example.currencyconverterapp.common

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.example.currencyconverterapp.common.PreferenceHelper.Companion.LAST_REFRESH_TIME_KEY
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PreferenceHelperTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferenceHelper: PreferenceHelper

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences(
            PreferenceHelper.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        preferenceHelper = PreferenceHelper(context)

        sharedPreferences.edit().clear().commit()
    }

    @Test
    fun `test saveLastRefreshTime should default value`() {
        val time = sharedPreferences.getLong(LAST_REFRESH_TIME_KEY, 0L)

        Assert.assertEquals(0L, time)
    }

    @Test
    fun testSaveLastRefreshTime() {
        val time = System.currentTimeMillis()
        preferenceHelper.saveLastRefreshTime(time)

        val lastRefreshTime = sharedPreferences.getLong(LAST_REFRESH_TIME_KEY, 0L)

        Assert.assertEquals(time, lastRefreshTime)
    }

    @Test
    fun `test getLastRefreshTime should return the updated time`() {
        val time = System.currentTimeMillis()+1000
        sharedPreferences.edit().putLong(LAST_REFRESH_TIME_KEY, time).apply()

        val lastRefreshTime = preferenceHelper.getLastRefreshTime()

        Assert.assertEquals(time, lastRefreshTime)
    }

    @Test
    fun `test getLastRefreshTime with max long value`() {
        val time = Long.MAX_VALUE
        sharedPreferences.edit().putLong(LAST_REFRESH_TIME_KEY, time).apply()

        val lastRefreshTime = preferenceHelper.getLastRefreshTime()

        Assert.assertEquals(time, lastRefreshTime)
    }

    @Test
    fun `test getLastRefreshTime with max negative value`() {
        val time = -1L
        sharedPreferences.edit().putLong(LAST_REFRESH_TIME_KEY, time).apply()

        val lastRefreshTime = preferenceHelper.getLastRefreshTime()

        Assert.assertEquals(time, lastRefreshTime)
    }

    @After
    fun tearDown() {
        sharedPreferences.edit().clear().commit()
    }
}
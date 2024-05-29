package com.example.currencyconverterapp.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.currencyconverterapp.ui.MainActivity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class NetworkUtilityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `test IsInternet returns True ForConnectedInternet`() {
        val networkInfo = mock(Network::class.java)
        val connectivityManager = mock(ConnectivityManager::class.java)
        `when`(connectivityManager.activeNetwork).thenReturn(networkInfo)
        val context = mock(Context::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)

        val result = NetworkUtility.isInternet(context)

        assertTrue(result)
    }

    @Test
    fun `test IsInternet returns False For Connected Internet`() {
        val connectivityManager = mock(ConnectivityManager::class.java)
        `when`(connectivityManager.activeNetwork).thenReturn(null)
        val context = mock(Context::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)

        val result = NetworkUtility.isInternet(context)
        assertFalse(result)
    }
}

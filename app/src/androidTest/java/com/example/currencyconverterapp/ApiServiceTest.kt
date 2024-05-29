package com.example.currencyconverterapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencyconverterapp.Mocks.mockResponseBody
import com.example.currencyconverterapp.data.apiservice.ApiService
import com.example.currencyconverterapp.data.model.CurrencyRatesResponseDto
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build().create(ApiService::class.java)
    }

    private fun parseCurrencyRatesResponse(mockResponseBody: String): CurrencyRatesResponseDto {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(CurrencyRatesResponseDto::class.java)
        val expectedResponse: CurrencyRatesResponseDto = jsonAdapter.fromJson(mockResponseBody)!!
        return expectedResponse
    }

    @Test
    fun `test getCurrencies fetchGetCurrencies expected mock response`() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(mockResponseBody)
        }
        mockWebServer.enqueue(mockResponse)

        val expected = parseCurrencyRatesResponse(mockResponseBody)

        val response = apiService.fetchCurrencyRates()
        mockWebServer.takeRequest()

        Assert.assertEquals(expected, response)
    }

    @Test
    fun `test fetchGetCurrencies returns expected json response`() = runTest {
        val content = Helper.readFileResources("/currency_rate.json")
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        val expected = parseCurrencyRatesResponse(content)

        val response = apiService.fetchCurrencyRates()
        mockWebServer.takeRequest()

        Assert.assertEquals(expected, response)
    }

    @Test(expected = HttpException::class)
    fun `test fetchGetCurrencies throws Exception`() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(404)
            setBody("Something went wrong")
        }
        mockWebServer.enqueue(mockResponse)

        val response = apiService.fetchCurrencyRates()
        mockWebServer.takeRequest()

        Assert.assertEquals("Something went wrong", response)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
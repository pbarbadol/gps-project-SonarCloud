package com.unex.asee.ga02.beergo

import com.google.gson.GsonBuilder
import com.unex.asee.ga02.beergo.api.BeerApiInterface
import com.unex.asee.ga02.beergo.data.api.BeerApi
import junit.framework.TestCase
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ApiServiceUnitTest {
    @Test
    @Throws(IOException::class)
    fun getBeerTest() {
        val mockWebServer = MockWebServer()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("").toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"IPA\",\n" +
                "    \"description\": \"A classic American pale ale.\",\n" +
                "    \"brewery\": \"Sierra Nevada\",\n" +
                "    \"abv\": 4.6,\n" +
                "    \"ibu\": 52\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"Lager\",\n" +
                "    \"description\": \"A refreshing German lager.\",\n" +
                "    \"brewery\": \"Beck's\",\n" +
                "    \"abv\": 4.5,\n" +
                "    \"ibu\": 20\n" +
                "  }\n" +
                "]")
        mockWebServer.enqueue(mockedResponse)

        val service: BeerApiInterface = retrofit.create(BeerApiInterface::class.java)

        val call: Call<List<BeerApi>> = service.getBeers(1)

        val response: Response<List<BeerApi>>? = call.execute()

        TestCase.assertTrue(response != null)
        TestCase.assertTrue(response!!.isSuccessful)

        //Step8bis: check the body content
        val beers: List<BeerApi>? = response.body()
        Assert.assertFalse(beers!!.isEmpty())
        TestCase.assertTrue(beers.size == 2)
        mockWebServer.shutdown()
    }

    @Test
    fun getDetailBeerTest() {
        val mockWebServer = MockWebServer()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("").toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody("{ \"id\": 1, \"name\": \"Sample Beer\", \"ingredients\": { \"malt\": [{\"name\": \"Pale Malt\", \"amount\": {\"value\": 1.0, \"unit\": \"kg\"}}, {\"name\": \"Extra Malt\", \"amount\": {\"value\": 0.5, \"unit\": \"kg\"}}], \"hops\": [{\"name\": \"Cascade Hops\", \"amount\": {\"value\": 50, \"unit\": \"g\"}}] } }")
        mockWebServer.enqueue(mockedResponse)

        val service: BeerApiInterface = retrofit.create(BeerApiInterface::class.java)

        val call: Call<BeerApi> = service.getBeerDetails(1)

        val response: Response<BeerApi>? = call.execute()

        TestCase.assertTrue(response != null)
        TestCase.assertTrue(response!!.isSuccessful)

        // Step8bis: check the body content
        val beer: BeerApi? = response.body()

        // Step9: check the properties of the BeerApi object
        TestCase.assertNotNull(beer)

        TestCase.assertEquals("Sample Beer", beer?.name)
        TestCase.assertNotNull(beer?.ingredients)

        // Adjust the verification based on the correct structure of your model
        TestCase.assertNotNull(beer?.ingredients?.malt)
        TestCase.assertNotNull(beer?.ingredients?.hops)

        // Assuming malt and hops are lists, you may need to adjust this part based on your actual model structure
        TestCase.assertTrue(beer?.ingredients?.malt?.isNotEmpty() ?: false)
        TestCase.assertTrue(beer?.ingredients?.hops?.isNotEmpty() ?: false)

        // For example, checking the first malt
        TestCase.assertEquals("Pale Malt", beer?.ingredients?.malt?.get(0)?.name)
        TestCase.assertEquals(1.0, beer?.ingredients?.malt?.get(0)?.amount?.value)
        TestCase.assertEquals("kg", beer?.ingredients?.malt?.get(0)?.amount?.unit)

        // For example, checking the first hops
        TestCase.assertEquals("Cascade Hops", beer?.ingredients?.hops?.get(0)?.name)
        TestCase.assertEquals(50.0, beer?.ingredients?.hops?.get(0)?.amount?.value)
        TestCase.assertEquals("g", beer?.ingredients?.hops?.get(0)?.amount?.unit)

        mockWebServer.shutdown()
    }

}
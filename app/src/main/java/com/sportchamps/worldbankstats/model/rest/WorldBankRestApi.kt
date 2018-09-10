package com.sportchamps.worldbankstats.model.rest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import javax.xml.transform.Result

/*
  * Module connecting to network and making http call
  * Module is written Using Retrofit component
  * Only two API calls has been made whih will fetch required info
  * If more info is required, new API's can be added here
 */
class WorldBankRestApi {
    companion object {
        val BASE_URL = "http://api.worldbank.org/v2/"
        fun getRestClientService(): WorldBankDataService {
            val builder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
           return  builder.create(WorldBankDataService::class.java)
        }
    }

    interface WorldBankDataService{
        @GET("countries/")
        fun getCountriesData(@Query("page")pageNumber:Int,@Query("format")
        datatype:String): Call<ResponseBody>

        @GET("countries/indicators/NY.GDP.MKTP.CD/")
        fun getCountriesGdpData(@Query("page")pageNumber:Int,@Query("format")
        datatype:String): Call<ResponseBody>
    }
}
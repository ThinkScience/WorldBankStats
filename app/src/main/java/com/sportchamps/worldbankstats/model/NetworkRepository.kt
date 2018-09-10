package com.sportchamps.worldbankstats.model

import android.util.Log
import com.sportchamps.worldbankstats.BuildConfig
import com.sportchamps.worldbankstats.common.util.AppConstants
import com.sportchamps.worldbankstats.common.util.CommonUtil
import com.sportchamps.worldbankstats.common.util.CommonUtil.Companion.getWorldGdpPojo
import com.sportchamps.worldbankstats.common.util.CommonUtil.Companion.getWorldInfoPojo
import com.sportchamps.worldbankstats.databases.CountryInfo
import com.sportchamps.worldbankstats.databases.CountryInfoDao
import com.sportchamps.worldbankstats.databases.WorldBankDataBase
import com.sportchamps.worldbankstats.model.data.pojo.PageHeader
import com.sportchamps.worldbankstats.model.data.pojo.worlddata.WorldData
import com.sportchamps.worldbankstats.model.data.pojo.worldgdp.WorldGdp
import com.sportchamps.worldbankstats.model.rest.WorldBankRestApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/*
 * Module to handle Network related business logic of
 * Model component. It is responsible for making network
 * related calls and updating DB accordingly
 */
class NetworkRepository(var mvInteractionInterface: UserRepository.MVInteractionInterface?) {
    val TAG: String = "SportChampsD-NRepos"
    val countryInfoDao: CountryInfoDao? =  WorldBankDataBase.getInstance()?.getCountryInfoDao()

    /*
     * SYNC device with N/w data. First get
     * GDP data and then get country Info
     * as country info data is required later
     * to update table filled by GDP data RestAPi
     */
    fun syncDeviceWithNetwork(){
        getCountriesGdpData()
        getCountriesInfo()
    }

    /*
       * Make RestAPi call to n/w and get GDP related
       * data of countries. Demo REST API call is
        * https://api.worldbank.org/v2/countries/ind/indicators/NY.GDP.MKTP.CD?page=1&format=json
     */
    private fun getCountriesGdpData(){
        var countryGdpDataList:MutableList<WorldGdp> = mutableListOf<WorldGdp>()
        lateinit var networkResponse: Call<ResponseBody>
        lateinit var response :Response<ResponseBody>
        var bufferString = StringBuffer()
        val numPages = getNetworkPageCount(AppConstants.Companion.OBJECT_TYPE.GDP)
        for(itr in 1..numPages) {
            bufferString.delete(0,bufferString.length)
            networkResponse = WorldBankRestApi.getRestClientService().getCountriesGdpData((itr+1),
                    "json")
            response = networkResponse.execute()
            if (response.isSuccessful) {
                Log.d(TAG, " Got successful response from API for PAGE NUM : ${itr}")
                bufferString.append(response.body()?.string())
                var worldGdp:WorldGdp = getWorldGdpPojo(bufferString)
                if(null != worldGdp) {
                    countryGdpDataList.add(worldGdp)
                    saveToDb(worldGdp)
                }
            }
            if(itr%4 == 0){
                val progressPercent:Float = (itr/numPages.toFloat())*100
                Log.i(TAG, "Progress Percentage : $progressPercent")
                mvInteractionInterface?.onProgressChange(progressPercent.toInt())
            }
        }
    }

    /*
       * Make RestAPi call to n/w and get GDP related
       * data of countries.Demo  API Call is :
       * https://api.worldbank.org/v2/countries?page=1&format=json
     */
    private fun getCountriesInfo() {
        lateinit var networkResponse: Call<ResponseBody>
        lateinit var response :Response<ResponseBody>
        var worldDataList:MutableList<WorldData> = mutableListOf<WorldData>()
        var bufferString = StringBuffer()
        val numPages = getNetworkPageCount(AppConstants.Companion.OBJECT_TYPE.INFO)
        Log.d(TAG, " Page Count for Country Info request : $numPages")
        for(itr in 1..numPages) {
            networkResponse = WorldBankRestApi.getRestClientService().getCountriesData((itr+1),
                    "json")
            response = networkResponse.execute()
            bufferString.delete(0,bufferString.length)
            if (response.isSuccessful) {
                Log.d(TAG, " Got successful response from API for PAGE NUM : ${itr}")
                bufferString.append(response.body()?.string())
                var worldData:WorldData = getWorldInfoPojo(bufferString)
                worldDataList.add(worldData)
                updateDb(worldData)
                Log.d(TAG, " Printing Page Number : $itr  Information")
                val progressPercent:Float = (itr/numPages.toFloat())*100
                Log.d(TAG, "Progress Percentage : $progressPercent")
                mvInteractionInterface?.onProgressChange(progressPercent.toInt())
            }
        }
    }

    /*
     * Get Total number of pages to be searched for in API call
     * This info is present in JSON data recieved from REST API
     * call
     */
    private fun getNetworkPageCount(type:AppConstants.Companion.OBJECT_TYPE):Int{
        var bufferString = StringBuffer()
        var pageCount = 0
        val networkResponse = when(type) {
                AppConstants.Companion.OBJECT_TYPE.GDP -> WorldBankRestApi.getRestClientService()
                        .getCountriesGdpData(1,"json")
                AppConstants.Companion.OBJECT_TYPE.INFO -> WorldBankRestApi.getRestClientService()
                        .getCountriesData(1,"json")
                }
        val response = networkResponse.execute()
        if(response.isSuccessful) {
            val responseStringBuf:StringBuffer =  bufferString.append(response.body()?.string(),
                    0,1000)
            val pageHeader = CommonUtil.getPageHeader(responseStringBuf)
            pageCount = pageHeader.pages
            Log.d(TAG," Page Header info  Total Page count :  "+pageHeader.pages)
        }
        return pageCount
    }

    /*
      Save Data to Db
     */
    fun saveToDb(worldGdp: WorldGdp?){
        var countryInfoList = mutableListOf<CountryInfo>()
        with(worldGdp){
            Log.i(TAG," world GDP object List size : ${this?.countriesGdpData?.size}")
            this?.countriesGdpData?.forEach { countrygdp->run {
                    val countryInfo = CountryInfo()
                    countryInfo.year = countrygdp.date.toInt()
                    countryInfo.country = countrygdp.country.value
                    if(null != countrygdp.value)
                        countryInfo.gdp = countrygdp.value
                    Log.i(TAG, " data to be stored in DB : ${countryInfo?.gdp}," +
                            " ${countryInfo.country},${countryInfo.year}")
                countryInfoList.add(countryInfo)
                }
            }
        }
        val insertionResult = countryInfoDao?.insertCountryInfoList(countryInfoList)
        if(BuildConfig.DEBUG_INFO) {
            insertionResult?.forEach { result ->
                run {
                    Log.i(TAG, " Insertion data List in DB : " + result)
                }
            }
        }
    }

    /*
     * Values like Country and region will be updated
    */
    private fun updateDb(worldData: WorldData?){
        worldData?.countries?.forEach { country-> run{
            countryInfoDao?.updateRegion(country.region.value , country.name)
            }
        }
    }
}
package com.sportchamps.worldbankstats.model

import android.content.Context
import android.util.Log
import com.sportchamps.worldbankstats.common.util.CommonUtil
import com.sportchamps.worldbankstats.databases.WorldBankDataBase
import com.sportchamps.worldbankstats.model.data.pojo.Country
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import com.sportchamps.worldbankstats.model.data.pojo.WorldData

/*
 * This is a MODEL component of M V V M architecture
 * main responsibility of this module is to handle
  * buisness logic like populating data back to the View.
  * Here depending upon set condition either data fetched
  * from DB or Network. This module is created once during
   * start of application, elsewhere it's first created
   * instance is shared(Singleton)
 */
class UserRepository {

    companion object {
        var TAG:String = "SportChampsD-UserRepository"
        var userRepository:UserRepository? = null
        var context:Context? = null
        var mvInteractionInterface:MVInteractionInterface? = null
        val countryInfoDao =  WorldBankDataBase.getInstance()?.getCountryInfoDao()
        fun createInstance(context: Context?):UserRepository?{
            if(null == userRepository){
                this.context = context
                Log.i(TAG, " UserRepository instance created")
                userRepository = UserRepository()
            }
            return userRepository
        }

        fun getInstance(mvInteractionInterface: MVInteractionInterface):UserRepository?{
            this.mvInteractionInterface = mvInteractionInterface
            return userRepository
        }
    }

    /*
      * Load data from wither Network or database depending upon
       * last sync time. If Last sync time is more than 24h then
       * re-sync back
     */
    fun loadWorldData(year:Int){
        Thread(Runnable {
            with(NetworkRepository(mvInteractionInterface)) {
                if(CommonUtil.isDbSyncRequired(context)) {
                    countryInfoDao?.deleteAll() //Empty the DB
                    syncDeviceWithNetwork()
                }else{
                    Log.i(TAG, " DB sync is not required")
                }
                updateViewData(year)
            }
        }).start()
    }

    /*
     * Fetch data from DB and convert to View understandable
      * Objects
     */
    private fun updateViewData(year:Int){
        var regionGdp = 0.0
        var regionList = mutableListOf<RegionD>()
        var regionNameList:MutableList<String>? = countryInfoDao?.getRegionList()

        for(regionName in regionNameList!!){
            Log.d(TAG, " Searching for data in Year : $year , Region : $regionName")
            var regionCountriesData = (countryInfoDao?.getRegionCountriesForYear(regionName,year))
            var countryList = mutableListOf<Country>()

            for (regionCountry in regionCountriesData!!){
                countryList.add(Country(regionCountry.country, regionCountry.gdp))
            }
            countryList.forEach { country ->  regionGdp += country.gdp }
            regionList.add(RegionD(regionName,countryList.toMutableList(), regionGdp))
            regionGdp = 0.0
            countryList.clear()
        }
        mvInteractionInterface?.onJobCompleted( WorldData(regionList) )
    }

    interface MVInteractionInterface{
        fun onJobCompleted(liveData : WorldData)
        fun onProgressChange(progress:Int)
    }
}
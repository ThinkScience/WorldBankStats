package com.sportchamps.worldbankstats.common.util

import android.content.Context.MODE_PRIVATE
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.sportchamps.worldbankstats.model.data.pojo.PageHeader
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import com.sportchamps.worldbankstats.model.data.pojo.worldgdp.WorldGdp
import com.sportchamps.worldbankstats.model.data.pojo.worlddata.WorldData
import java.util.*

/*
 Common Utlity module for all modules in the project
 */
class CommonUtil {
    companion object {
        val million = 1000000.0
        val billion = 1000000000.0
        val trillion = 1000000000000.0
        fun shortGdpDenotion(gdpNum: Double):String{
            var retVal:String
            val dollarSign = "$ "
            if(gdpNum > million && gdpNum < billion){
                retVal = (dollarSign+(gdpNum/ million).toString() +" M")
            }else if(gdpNum > billion && gdpNum < trillion){
                retVal = (dollarSign+(gdpNum/ billion).toString() +" B")
            }else if(gdpNum > trillion){
                retVal = (dollarSign+(gdpNum/ trillion).toString() +" T")
            }else{
                retVal = dollarSign+gdpNum.toString()
            }
            return retVal
        }

        /*
         * Check if DB sync with Network is required or not
         * If Last DB sync has happened more than 24h ago then sync DB again
         */
        fun isDbSyncRequired( context:Context?):Boolean{
            var retValue = false
            val lastSyncDuration = System.currentTimeMillis() - getLastDbSyncTime(context)
            if(lastSyncDuration > AppConstants.APP_NETWORK_SYNC_TIME_MS){
                setDbSyncTime(context)
                retValue = true
            }
            return retValue
        }

        /*
         * Set Db sync data in Shared pref
         */
        fun setDbSyncTime(context:Context?){
            val editor = context?.getSharedPreferences("", MODE_PRIVATE)?.edit()
            editor?.putLong(AppConstants.DB_SYNC_SHARED_PREF_KEY, System.currentTimeMillis())
            editor?.apply()
        }

        /*
         * Get Last Db sync data
         */
        fun getLastDbSyncTime(context:Context?):Long{
            val sharedPref = context?.getSharedPreferences("", MODE_PRIVATE)
            var retVal = sharedPref?.getLong(AppConstants.DB_SYNC_SHARED_PREF_KEY, 100)
            if(null == sharedPref)
                retVal = 100
            return retVal!!
        }

        /*
          * WorldBank API call returns data in improper JSON
          * GET HEADER info from JSON
         */
        fun getPageHeader(networkData: StringBuffer):PageHeader{
            var headerString = ""
            var index = 1
            while(networkData[index] != '[' ){
                index++
            }
            headerString = networkData.subSequence(1,index-1) as String
            val gson = Gson()
            var pageHeader:PageHeader = gson.fromJson(headerString, PageHeader::class.java)
            Log.d("SportChampsD", " Header data : "+headerString)
            return pageHeader
        }

        /*
         * WorldBank API call returns data in improper JSON
         * FIX the json and get WorldGDP POJO from it
        */
        fun getWorldGdpPojo(networkJson:StringBuffer):WorldGdp{
            val missingHeadGdpJson = "{ "+
                    "\"countriesGdpData\":"
            val gson = Gson()
            var gsonData = gson.fromJson(fixJsonGetData(missingHeadGdpJson,networkJson).toString(), WorldGdp::class.java)
            return gsonData
        }

        /*
        * WorldBank API call returns data in improper JSON
        * FIX the json and get WORLDINFO POJO from it
       */
        fun getWorldInfoPojo(networkJson:StringBuffer):WorldData{
            val missingHeadInfoJson = "{ "+
                    "\"countries\":"
            val gson = Gson()
            var gsonData = gson.fromJson(fixJsonGetData(missingHeadInfoJson,networkJson).toString(), WorldData::class.java)
            return gsonData
        }

        /*
        * WorldBank API call returns data in improper JSON
        * FIX JSON by REMOVING HEADER PART and converting
        * it to required TYPE(WORLDGDP/WORLDINFO)
       */
        private fun fixJsonGetData(missingHeadJson:String, stringBufferData:StringBuffer):StringBuffer{
            var missingTailJson = "}"
            var index = 1
            while(stringBufferData[index] != '[' ){
                index++
            }
            stringBufferData.delete(0,index)
            stringBufferData.insert(0,missingHeadJson)
            index =  stringBufferData.length-1
            while(stringBufferData[index] != ']'){
                index--
            }
            stringBufferData.replace(index , index+1,missingTailJson)
            return stringBufferData
        }

        /*
          *SORT REGIOND object depending upon sort option
         */
        fun getSortedList(dataList:List<RegionD>,sortType:AppConstants.Companion.SORT_TYPE){
            Collections.sort(dataList, object : Comparator<RegionD> {
                override fun compare(obj1: RegionD, obj2: RegionD): Int = when(sortType){
                                    AppConstants.Companion.SORT_TYPE.GDP->(obj1.gdp.compareTo(obj2.gdp))
                                    AppConstants.Companion.SORT_TYPE.ALPHABETICAL-> (obj1.name.compareTo(obj2.name))
                            }
            })
        }
    }
}
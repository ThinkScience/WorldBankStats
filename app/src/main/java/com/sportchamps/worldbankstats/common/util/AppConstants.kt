package com.sportchamps.worldbankstats.common.util

class AppConstants {
    companion object {
        val APP_NETWORK_SYNC_TIME_MS = 24*60*60*1000 //SYNC every 24hrs
     //   val APP_NETWORK_SYNC_TIME_MS = 1*1*10*1000 //DEBUG SYNC every 10sec
        val DB_SYNC_SHARED_PREF_KEY:String = "DB_SYNC_SHARED_PREF_KEY"
        enum class OBJECT_TYPE { INFO , GDP }
        enum class SORT_TYPE{GDP, ALPHABETICAL}
    }
}
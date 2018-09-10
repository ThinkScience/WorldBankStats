package com.sportchamps.worldbankstats

import android.app.Application
import android.util.Log
import com.sportchamps.worldbankstats.databases.WorldBankDataBase
import com.sportchamps.worldbankstats.model.UserRepository

/*
   Entry point for the application
   here I instantisate module like UserRepository
   and DB so that it can stay in scope of application
 */
class WorldBankStatApplication:Application() {
    val tag:String = "SportChampsD-WbStats"

    override fun onCreate(){
        super.onCreate()
        Log.i(tag, " World Bank stats invoked")
        WorldBankDataBase.createInstance(this)
        UserRepository.createInstance(this)
    }
}
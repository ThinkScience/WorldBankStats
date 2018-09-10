package com.sportchamps.worldbankstats.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log

@Database(entities = arrayOf(CountryInfo::class), version = 1)
abstract class WorldBankDataBase :RoomDatabase() {
    abstract fun getCountryInfoDao():CountryInfoDao

    companion object {
        private var INSTANCE:WorldBankDataBase? = null
        fun createInstance(context: Context): WorldBankDataBase? {
            if (INSTANCE == null) {
                synchronized(WorldBankDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                            WorldBankDataBase::class.java, "worldbank.db")
                            .build()
                    Log.i("SportChamps-WorldBankDb", " Db instance created")
                }
            }
            return INSTANCE
        }

        fun getInstance():WorldBankDataBase?{
            return INSTANCE
        }
    }
}
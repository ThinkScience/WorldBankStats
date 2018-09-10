package com.sportchamps.worldbankstats.databases

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
/*
  TABLE OF our ROOM Database and it saves
  entries mentioned in Class CountryInfo
 */
@Entity
class CountryInfo(var year:Int, var country:String,var region:String, var gdp:Double) {
            constructor():this(1960,"ABCDE","UNIVERSE",0.0)

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
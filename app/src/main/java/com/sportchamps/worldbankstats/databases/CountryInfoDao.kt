package com.sportchamps.worldbankstats.databases

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
public interface CountryInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryInfo(country: CountryInfo):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountryInfoList(countryInfoList: List<CountryInfo>):Array<Long>

    @Query( " SELECT * from CountryInfo WHERE year=:year")
    fun getCountryInfoByYear(year:Int):MutableList<CountryInfo>

    @Query( " SELECT * from CountryInfo WHERE country=:country")
    fun getCountryInfoByName(country:String):MutableList<CountryInfo>

    @Query( " SELECT * from CountryInfo WHERE region=:region")
    fun getCountryInfoByRegion(region: String):MutableList<CountryInfo>

    @Query("SELECT * from CountryInfo WHERE region=:region AND year =:year")
    fun getRegionCountriesForYear(region:String, year: Int):MutableList<CountryInfo>

    @Query( "SELECT DISTINCT region from CountryInfo")
    fun getRegionList(): MutableList<String>

    @Query("SELECT * from CountryInfo")
    fun getAll(): MutableList<CountryInfo>

    @Query("UPDATE CountryInfo SET region=:region WHERE country = :country")
    fun updateRegion(region:String, country:String):Int

    @Query("UPDATE CountryInfo SET gdp=:gdp WHERE country = :country")
    fun updateGdp(gdp:Double, country:String):Int

    @Query("DELETE from CountryInfo")
    fun deleteAll():Int
}
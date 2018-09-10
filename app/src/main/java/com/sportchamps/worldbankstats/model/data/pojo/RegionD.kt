package com.sportchamps.worldbankstats.model.data.pojo

import java.io.Serializable

data class RegionD(var name:String, var countryList:List<Country>,var gdp:Double) :Serializable{

}